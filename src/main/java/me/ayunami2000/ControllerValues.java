package me.ayunami2000;

public class ControllerValues {
    public static boolean enabled=false;
    public static float lvibr=0;
    public static float rvibr=0;

    public ControllerValues(){

    }

    public boolean getEnabled(){
        return enabled;
    }
    public boolean setEnabled(boolean v){
        enabled=v;
        return enabled;
    }
    public float getLvibr(){
        return lvibr;
    }
    public float setLvibr(float v){
        lvibr=v;
        return lvibr;
    }
    public float addLvibr(float v){
        lvibr+=v;
        return lvibr;
    }
    public float getRvibr(){
        return rvibr;
    }
    public float setRvibr(float v){
        rvibr=v;
        return rvibr;
    }
    public float addRvibr(float v){
        rvibr+=v;
        return rvibr;
    }
}
