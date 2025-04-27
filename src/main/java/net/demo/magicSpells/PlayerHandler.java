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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerHandler implements Listener{

    JavaPlugin plugin = JavaPlugin.getPlugin(MagicSpells.class);

    private BukkitTask resetTask;
    private BukkitTask delayTask;
    public static HashMap<UUID, Integer> spell1_Map = new HashMap<>();
    public static HashMap<UUID, Integer> spell_Map = new HashMap<>();
    int current = spell_Map.getOrDefault(spell_Map,0);
    int current1 = spell1_Map.getOrDefault(spell1_Map,0);

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        ItemStack stack = new ItemStack(Material.BREEZE_ROD);
        if (!player.hasPlayedBefore()) {
            player.getInventory().addItem(stack);
        }
        player.setAllowFlight(true);
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
    }



    @EventHandler
    public void spellCast(PlayerInteractEvent e){
        Player player = e.getPlayer();
        World world = player.getWorld();
        Location location = player.getLocation();
        UUID uuid = player.getUniqueId();

        if(player.getInventory().getItemInMainHand().getType()==Material.BREEZE_ROD){

            if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){
                current1++;
                spell_Map.put(uuid, current1);
                //player.sendMessage(String.valueOf(spell_Map));
                resetSpells(player);
            }
            if(e.getAction().equals(Action.LEFT_CLICK_AIR)){
                current++;
                spell1_Map.put(uuid, current);
                //player.sendMessage(String.valueOf(spell1_Map));
                resetSpells(player);
            }

            if(current1==1 && current==1){
                List<Entity> nearestEntities = player.getNearbyEntities(10d,0,10d);
                for(Entity entity : nearestEntities){
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).damage(3.5);
                    }
                }
                double radius = 1.5;

                for (int i = 0; i < 360; i += 10) {
                    double angle = Math.toRadians(i);
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    Location particleLoc = location.clone().add(x, 1.0, z); // высота = 1 блок над землёй
                    world.spawnParticle(Particle.SOUL_FIRE_FLAME, particleLoc, 0, 0, 0, 0, 0);
                }
            }
            if (current==2) {
                PotionEffect effect = new PotionEffect(PotionEffectType.INSTANT_HEALTH, 2,1);
                effect.apply(player);
                double radius = 1.2;

                for (int i = 0; i < 360; i += 10) {
                    double angle = Math.toRadians(i);
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    Location particleLoc = location.clone().add(x, 1.0, z); // высота = 1 блок над землёй
                    world.spawnParticle(Particle.END_ROD, particleLoc, 0, 0, 0, 0, 0);
                }
            }
            if(current==2 && current1==1){
                Block block = player.getTargetBlock(null,5);
                Location blockLocation = block.getLocation();
                world.spawnEntity(blockLocation, EntityType.LIGHTNING_BOLT);
                String loc = blockLocation+"";
                player.sendMessage(loc);
            }
            if(current==3){
                Block block = player.getTargetBlock(null,5);
                Location blockLoc = block.getLocation();
                player.teleport(blockLoc);
            }
        }
    }
    private void resetSpells(Player player) {
        if(resetTask != null && resetTask.isCancelled()){resetTask.cancel();}

        resetTask = new BukkitRunnable() {
            @Override
            public void run() {
                current=0;
                current1=0;
            }
        }.runTaskLater(plugin, 40);
    }
}

