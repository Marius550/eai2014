package de.java.ejb;

import java.util.Date;

import javax.ejb.Remote;

import de.java.ws.PrescriptionStatisticsDO;
@Remote
public interface PrescriptionStatisticsService {

PrescriptionStatisticsDO getStatisticsFromJava(Date dateFrom, Date dateTo);

PrescriptionStatisticsDO getStatisticsFromDotNet(Date dateFrom, Date dateTo);

}