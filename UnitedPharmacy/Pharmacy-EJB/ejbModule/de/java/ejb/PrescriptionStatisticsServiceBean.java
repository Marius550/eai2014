package de.java.ejb;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateless;
import javax.jms.Message;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import de.java.jms.MessageSender;
import de.java.jms.PrescriptionMessage;
import de.java.ws.DrugResourceClientDotNet;
import de.java.ws.PrescriptionStatisticsClientDotNet;
import de.java.ws.PrescriptionStatisticsDO;

@Stateless
public class PrescriptionStatisticsServiceBean implements PrescriptionStatisticsService {

  // URLs of the webservices	
  private static final String BASE_URI_DOTNET = "http://localhost:1054/RestService";
  
  /**
   * Prepares a rest-webservice-proxy for connection to C.Sharpe
   * @return WebService proxy for C.Sharpe
   */
  private PrescriptionStatisticsClientDotNet prepareDrugResourceClientDotNet(){
	  RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	  ResteasyWebTarget target = new ResteasyClientBuilder().build().target(BASE_URI_DOTNET);
	  return target.proxy(PrescriptionStatisticsClientDotNet.class);
  }

	@Override
	public PrescriptionStatisticsDO getStatisticsFromJava(Date dateFrom, Date dateTo) {
		Message JaVaReply = MessageSender.sendWithReply(new PrescriptionMessage(dateFrom, dateTo));
		PrescriptionStatisticsDO result = new PrescriptionStatisticsDO();
		try {
			result.setTotalNumberOfPrescriptions(JaVaReply.getIntProperty(
							"totalNumberOfPrescriptions"));
//			TODO: Remove this
//			result.setAverageItemsPerPrescription(JaVaReply.getDoubleProperty(
//					"averageNumberOfItems"));
//			result.setAverageTimespanToFulfilment(JaVaReply.getDoubleProperty(
//					"averageTimespanToFulfilment"));
		} catch (Exception e) {
			// TODO: Do something
		}
		return result;	
	}

	@Override
	public PrescriptionStatisticsDO getStatisticsFromDotNet(final Date dateFromParam,
			final Date dateToParam) {
		//TODO: Remove this:
		return null;
		
		
//		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
//		Object dateObject = new Object(){
//			@SuppressWarnings("unused")
//			public String dateFrom = sdf.format(dateFromParam);
//			@SuppressWarnings("unused")
//			public String dateTo = sdf.format(dateToParam);
//		};
		//return prepareDrugResourceClientDotNet().getPrescriptionStatistics(dateObject);
	}

}
