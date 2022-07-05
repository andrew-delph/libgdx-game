package entity.attributes;

import entity.attributes.msc.Health;
import org.junit.Test;

public class testEqual {

  @Test
  public void testHealthEqual() {
    Health h1 = new Health(100);
    Health h2 = new Health(100);

    assert h1.equals(h2);
  }
}
