package de.groodian.hyperiorlobby.gui;

import de.groodian.hyperiorcore.gui.GUI;
import de.groodian.hyperiorcore.gui.GUIRunnable;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.user.DailyBonus;
import de.groodian.hyperiorcore.user.DailyBonusType;
import de.groodian.hyperiorcore.user.Rank;
import de.groodian.hyperiorcore.user.User;
import de.groodian.hyperiorcore.util.HSound;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.hyperiorcore.util.Time;
import java.time.OffsetDateTime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class DailyBonusGUI extends GUI {

    public DailyBonusGUI() {
        super(Component.text("Tägliche Belohnung"), 27);
    }

    @Override
    protected void onOpen() {
        ItemStack white = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§a").build();
        ItemStack gray = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§a").build();

        putItemsDelayed(white, new int[]{0, 8, 18, 26}, 3);
        putItemsDelayed(gray, new int[]{1, 7, 19, 25}, 6);
        putItemsDelayed(gray, new int[]{2, 6, 20, 24, 9, 17}, 9);

        addRewards();

        new HSound(Sound.ENTITY_HORSE_ARMOR).playFor(player);
    }

    private void addRewards() {
        User user = HyperiorCore.getUserManager().get(player.getUniqueId());

        addReward(user, user.getDailyBonus(), DailyBonusType.PLAYER, null, null, DailyBonus.DAILY_BONUS_COINS,
                new ItemBuilder(Material.SUGAR).setName("§7§lSpieler-Belohnung"), 12);
        addReward(user, user.getDailyBonusVIP(), DailyBonusType.VIP, "dailybonus.vip", "VIP", DailyBonus.DAILY_BONUS_VIP_COINS,
                new ItemBuilder(Material.GLOWSTONE_DUST).setName("§e§lVIP-Belohnung"), 14);
    }

    private void addReward(User user, OffsetDateTime offsetDateTime, DailyBonusType dailyBonusType, String permission, String rankName,
                           int coins, ItemBuilder itemBuilder, int slot) {
        if (user.has(permission)) {
            if (user.canCollect(offsetDateTime)) {
                GUIRunnable guiRunnable = () -> {
                    HyperiorCore.getDailyBonus().collect(player, dailyBonusType);
                    new HSound(Sound.ENTITY_PLAYER_LEVELUP).playFor(player);
                    update();
                };

                putItem(itemBuilder.setLore("", rewardString(coins), "", "§aKlicke um die Belohnung einzusammeln.").build(), slot,
                        guiRunnable);
            } else {
                putItem(itemBuilder.setLore("", rewardString(coins), "",
                        "§cDu kannst die Belohnung erst wieder in " + Time.timeLeftString(OffsetDateTime.now(),
                                offsetDateTime.plusMinutes(DailyBonus.COLLECT_WAIT_MINUTES)) + " einsammeln.").build(), slot);
            }
        } else {
            TextComponent.Builder rankLine = Component.text().append(Component.text("mit dem ", NamedTextColor.GRAY));
            for (Rank allRank : Rank.RANKS) {
                if (allRank.name().equals(rankName)) {
                    rankLine.append(Component.text(allRank.name() + "-Rang", allRank.color()));
                    break;
                }
            }
            rankLine.append(Component.text(" einsammeln!"));

            putItem(itemBuilder.setLore("", rewardString(coins), "", "§7Diese Belohnung kannst du nur").addLore(rankLine.build()).build(),
                    slot);
        }
    }

    private String rewardString(int coins) {
        return "§7Belohnung: §e+" + coins + " §6Coins";
    }

    @Override
    public void onUpdate() {
        addRewards();
    }

}
