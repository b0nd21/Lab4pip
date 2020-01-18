package lab4;

public class AreaCheck {
    public static boolean isInArea(Dot dot){
        Double x = dot.getX();
        Double y = dot.getY();
        Double r = dot.getR();
        if(x<=0&&y<=0 && x>=-r&&y>=-r/2) return true;
        if(x<=0 && y>=0 && y<= x*2+r) return true;
        if(x>=0 && y <=0 && x*x+y*y <r*r/4) return true;
        return false;
    }
}
