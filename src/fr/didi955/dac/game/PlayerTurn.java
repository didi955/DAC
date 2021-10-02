package fr.didi955.dac.game;

import fr.didi955.dac.DAC;
import fr.didi955.dac.spells.SpellUnit;
import fr.didi955.dac.tasks.Afk;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class PlayerTurn {

    private Player playerTurn;
    private int position;
    private Afk afk;
    private int nextPositionRequired = 1;

    public PlayerTurn() {
    }

    public Player getPlayer() {
        return playerTurn;
    }

    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setNextPositionRequired(int nextPositionRequired) {
        this.nextPositionRequired = nextPositionRequired;
    }

    public int getNextPositionRequired() {
        return nextPositionRequired;
    }

    public void chooseFirstPlayer(){
        position = new Random().nextInt(DAC.getInstance().getPlayersGameList().size());
        playerTurn = DAC.getInstance().getPlayersGameList().get(position);
        playerTurn.teleport(Locations.DIVING_PLATFORM.getLocation());
        SpellUnit.giveItems(playerTurn);
    }

    public void makeAnnouncement(){
        playerTurn.sendMessage("§eC'est votre tour !");
        playerTurn.sendTitle("§eC'est votre tour !", "§6Bonne chance", 10, 70, 20);
        for(Player pls : DAC.getInstance().getPlayersGameList()){
            pls.sendMessage("§6C'est au tour de §c" + this.playerTurn.getDisplayName());
            pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        }

    }

    public void initNextPlayer(int i){
        playerTurn.getInventory().clear();
        if(afk != null){
            Bukkit.getScheduler().cancelTask(this.afk.getTaskId());
        }
        if(i == 1){
            if(position == DAC.getInstance().getPlayersGameList().size()-1){
                position = 0;
            }
            else
            {
                position +=1;
            }
        }
        else
        {
            if(position == DAC.getInstance().getPlayersGameList().size()-1){
                position = 1;
            }
            else
            {
                position = 0;
            }
        }
        setPlayerTurn(DAC.getInstance().getPlayersGameList().get(position));
        makeAnnouncement();
        teleportPlayer();
        setNextPositionRequired(1);
        SpellUnit.giveItems(playerTurn);
        Afk afk = new Afk(playerTurn);
        afk.runTaskTimer(DAC.getInstance(), 0L, 20L);
        this.afk = afk;
    }

    public void teleportPlayer(){
        playerTurn.teleport(Locations.DIVING_PLATFORM.getLocation());
    }

    public String getPlayerName(){
        if(this.playerTurn == null){
            return "Choix en cours";
        }
        return this.playerTurn.getDisplayName();
    }
}
