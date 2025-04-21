package net.demo.magicSpells;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerHandler implements Listener{

    JavaPlugin plugin = JavaPlugin.getPlugin(MagicSpells.class);

    public int spell;
    public int spell1;
    private BukkitTask resetTask;
    private BukkitTask delayTask;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        ItemStack stack = new ItemStack(Material.BREEZE_ROD);
        if (!player.hasPlayedBefore()) {
            player.getInventory().addItem(stack);
        }
        player.setAllowFlight(true);
    }

    @EventHandler
    public void spellCast(PlayerInteractEvent e){
        Player player = e.getPlayer();
        World world = player.getWorld();
        Location location = player.getLocation();

        if(player.getInventory().getItemInMainHand().getType()==Material.BREEZE_ROD){

            if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){
                spell++;
                player.sendMessage(String.valueOf(spell));
                resetSpells(player);
            }
            if(e.getAction().equals(Action.LEFT_CLICK_AIR)){
                spell1++;
                player.sendMessage(String.valueOf(spell1));
                resetSpells(player);
            }

            if(spell==1 && spell1==1){
                world.spawnParticle(Particle.SOUL_FIRE_FLAME, location, 20, 0.1,0,0.1);
                List<Entity> nearestEntities = player.getNearbyEntities(10d,0,10d);
                for(Entity entity : nearestEntities){
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).damage(3.5);
                    }
                }
            }
            if (spell==2) {
                PotionEffect effect = new PotionEffect(PotionEffectType.INSTANT_HEALTH, 2,1);
                effect.apply(player);
            }
            if(spell==2 && spell1==1){
                Block block = player.getTargetBlock(null,5);
                Location blockLocation = block.getLocation();
                world.spawnEntity(blockLocation, EntityType.LIGHTNING_BOLT);
                String loc = blockLocation+"";
                player.sendMessage(loc);
            }
        }
    }
    private void resetSpells(Player player) {
        if(resetTask != null && resetTask.isCancelled()){resetTask.cancel();}

        resetTask = new BukkitRunnable() {
            @Override
            public void run() {
                spell=0;
                spell1=0;
                //player.sendMessage(ChatColor.GOLD + "Spells variables were set to null");
            }
        }.runTaskLater(plugin, 40);
    }
}

