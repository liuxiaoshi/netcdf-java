package thredds.server.ncss.view.dsg;

import thredds.server.ncss.exception.*;
import thredds.server.ncss.format.SupportedFormat;
import thredds.server.ncss.params.NcssParamsBean;
import thredds.server.ncss.view.dsg.station.StationSubsetWriterCSV;
import thredds.server.ncss.view.dsg.station.StationSubsetWriterXML;
import ucar.nc2.constants.FeatureType;
import ucar.nc2.ft.FeatureDatasetPoint;
import ucar.nc2.util.DiskCache2;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.UnsupportedOperationException;

/**
 * Created by cwardgar on 2014/05/21.
 */
public abstract class DsgSubsetWriterFactory {
    public static DsgSubsetWriter newInstance(FeatureDatasetPoint fdPoint, NcssParamsBean ncssParams,
            DiskCache2 diskCache, OutputStream out, FeatureType featureType, SupportedFormat format)
            throws NcssException, XMLStreamException, IOException {
        if (!featureType.isPointFeatureType()) {
            throw new NcssException(String.format("Expected a point feature type, not %s", featureType));
        }

        switch (featureType) {
            case STATION:
                return newStationInstance(fdPoint, ncssParams, diskCache, out, format);
            default:
                throw new UnsupportedOperationException(
                        String.format("%s feature type is not yet supported.", featureType));
        }
    }

    public static DsgSubsetWriter newStationInstance(FeatureDatasetPoint fdPoint, NcssParamsBean ncssParams,
            DiskCache2 diskCache, OutputStream out, SupportedFormat format)
            throws XMLStreamException, NcssException, IOException {
        switch (format) {
            case XML_STREAM:
            case XML_FILE:
                return new StationSubsetWriterXML(fdPoint, ncssParams, out);
            case CSV_STREAM:
            case CSV_FILE:
                return new StationSubsetWriterCSV(fdPoint, ncssParams, out);
            case NETCDF3:
//                w = new WriterNetcdf(NetcdfFileWriter.Version.netcdf3, out);
                return null;
            case NETCDF4:
//                w = new WriterNetcdf(NetcdfFileWriter.Version.netcdf4, out);
                return null;
            case WATERML2:
//                w = new WriterWaterML2(out);
                return null;
            default:
                throw new UnsupportedResponseFormatException("Unknown result type = " + format.getFormatName());
        }
    }

    private DsgSubsetWriterFactory() { }
}
