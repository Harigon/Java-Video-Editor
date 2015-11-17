package src.thirdPartyLibraries.audio;

import src.thirdPartyLibraries.audio.jl.player.advanced.AdvancedPlayer;



public class SoundMP3 {

	
	public int mp3Id;
	public AdvancedPlayer player;
	public Thread thread;
	
	public SoundMP3(int soundId, AdvancedPlayer player){
		this.player = player;
		this.mp3Id = soundId;
		MP3.MP3s.add(this);
	}
}
