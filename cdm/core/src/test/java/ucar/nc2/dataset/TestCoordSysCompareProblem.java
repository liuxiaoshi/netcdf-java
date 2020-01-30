/* Copyright Unidata */
package ucar.nc2.dataset;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Formatter;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;
import ucar.nc2.constants._Coordinate;
import ucar.nc2.util.CompareNetcdf2;
import ucar.nc2.util.CompareNetcdf2.ObjFilter;
import ucar.unidata.util.test.category.NeedsCdmUnitTest;

/**
 * Compare CoordSysBuilder (new) and CoordSystemBuilderImpl (old)
 */
@Category(NeedsCdmUnitTest.class)
public class TestCoordSysCompareProblem {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Test
  public void compareProblemFile() throws IOException {
    String filename = "file:D:/cdmUnitTest/conventions/cf/SUPER-NATIONAL_latlon_IR_20070222_1600.nc";
    showOrg(filename);
    showNew(filename);
    compare(filename);
  }

  void compare(String fileLocation) throws IOException {
    System.out.printf("Compare %s%n", fileLocation);
    logger.info("TestCoordSysCompare on {}%n", fileLocation);
    try (NetcdfDataset org = NetcdfDataset.openDataset(fileLocation)) {
      try (NetcdfDataset withBuilder = NetcdfDatasets.openDataset(fileLocation)) {
        Formatter f = new Formatter();
        CompareNetcdf2 compare = new CompareNetcdf2(f, false, false, true);
        boolean ok = compare
            .compare(org, withBuilder, new ucar.nc2.dataset.TestCoordSysCompareMore.CoordsObjFilter(), false, false, true);
        System.out.printf("%s %s%n", ok ? "OK" : "NOT OK", f);
        System.out.printf("org = %s%n", org.getRootGroup().findAttValueIgnoreCase(_Coordinate._CoordSysBuilder, ""));
        System.out.printf("new = %s%n",
            withBuilder.getRootGroup().findAttValueIgnoreCase(_Coordinate._CoordSysBuilder, ""));
        assertThat(ok).isTrue();
      }
    }
  }

  private void showOrg(String fileLocation) throws IOException {

    try (NetcdfDataset org = NetcdfDataset.openDataset(fileLocation)) {
      Variable v = org.findVariable("lev");
      //Array data = v.read();
      //System.out.printf("data = %s%n", data);
    }
  }

  private void showNew(String fileLocation) throws IOException {

    try (NetcdfDataset withBuilder = NetcdfDatasets.openDataset(fileLocation)) {
      Variable v = withBuilder.findVariable("lev");
     // Array data = v.read();
      //System.out.printf("data = %s%n", data);
    }
  }

}



