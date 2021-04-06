package infra.map.chunk;

import infra.common.Coordinate;

public class ChunkRange {
    public static final int size = 50;
    public int bottom_x;
    public int bottom_y;
    public int top_x;
    public int top_y;
    public ChunkRange(Coordinate coordinate){
        if(coordinate.getX()<0){
            this.bottom_x  = (((coordinate.getX()/size))*size)-size;
        }
        else{
            this.bottom_x  = ((coordinate.getX()/size))*size;
        }

        if(coordinate.getY()<0){
            this.bottom_y  = (((coordinate.getY()/size))*size)-size;
        }
        else{
            this.bottom_y  = ((coordinate.getY()/size))*size;
        }

        this.top_y = this.bottom_y+size;
        this.top_x = this.bottom_x+size;

    }

    @Override
    public int hashCode() {
        return (this.bottom_x + "," + this.bottom_x+","+this.top_x+","+this.top_y).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChunkRange other = (ChunkRange) obj;
        return bottom_x == other.bottom_x && bottom_y == other.bottom_y && top_x == other.top_x && top_y == other.top_y;
    }

    public String tostring(){
        return this.bottom_x+","+this.bottom_y+","+this.top_x+","+this.top_y;
    }
}
