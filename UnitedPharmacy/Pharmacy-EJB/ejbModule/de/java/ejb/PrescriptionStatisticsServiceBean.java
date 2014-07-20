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
		try {
			Message JaVaReply = MessageSender.sendWithReply(new PrescriptionMessage(dateFrom, dateTo));
			PrescriptionStatisticsDO result = new PrescriptionStatisticsDO();
			result.setTotalNumberOfPrescriptions(JaVaReply.getIntProperty(
							"totalNumberOfPrescriptions"));
			result.setAverageNumberOfItemsPerPrescription(JaVaReply.getDoubleProperty(
					"averageNumberOfItemsPerPrescription"));
			result.setAverageTimeSpanOfFulfilment(JaVaReply.getLongProperty(
					"averageTimeSpanOfFulfilment"));
			return result;	
		} catch (Exception e) {
			// When anything during the connection fails, just return a null element. The view will handle anything else.
			System.out.println(e);
			return null;
		}
	}

	@Override
	public PrescriptionStatisticsDO getStatisticsFromDotNet(Date dateFrom,Date dateTo) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return prepareDrugResourceClientDotNet().getPrescriptionStatistics(sdf.format(dateFrom), sdf.format(dateTo));
		} catch (Exception e){
			// When anything during the connection fails, just return a null element. The view will handle anything else.
			System.out.println(e);
			return null;
		}
	}

}
