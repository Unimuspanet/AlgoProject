import java.util.*;

public class Trip {

  public int id;
  private String s1;
  private String s2;
  private String s3;
  private String s4;
  private String s5;
  private String s6;
  private String s7;
  private String s8;

  public Trip(String[] trip)   {
    this.id = Integer.parseInt(trip[0]);
    this.s1 = trip[1];
    this.s2 = trip[2];
    this.s3 = trip[3];
    this.s4 = trip[4];
    this.s5 = trip[5];
    this.s6 = trip[6];
    this.s7 = trip[7];
    this.s8 = trip[8];
  }

  public String[] toArray() {
      String[] arry = new String[]{
      String.valueOf(this.id),
      this.s1,
      this.s2,
      this.s3,
      this.s4,
      this.s5,
      this.s6,
      this.s7,
      this.s8,
    };
    return arry;
  }

}

class SortByID implements Comparator<Trip> {
  public int compare(Trip a, Trip b)
  {
      return a.id - b.id;
  }
}
