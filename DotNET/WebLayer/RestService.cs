using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.ServiceModel.Activation;
using System.Web;
using Pharmacy.BusinessLayer.Logic;
using WebLayer.Messages;

namespace WebLayer
{
    [AspNetCompatibilityRequirements(RequirementsMode = AspNetCompatibilityRequirementsMode.Allowed)]
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class RestService : IRestService
    {
        public MessageDrug[] Drugs()
        {
            return DrugService.GetAllDrugs().Select(d => new MessageDrug(d)
            {
                pendingQuantity = DrugService.GetQuantityPending(d),
                unfulfilledQuantity = PrescriptionService.GetQuantityUnfulfilledForDrug(d.PZN)
            }).ToArray();
        }

        public MessageDrug Drug(string pzn)
        {
            Pharmacy.BusinessLayer.Data.Drug d = DrugService.GetDrug(Int32.Parse(pzn));
            return new MessageDrug(d)
            {
                pendingQuantity = DrugService.GetQuantityPending(d),
                unfulfilledQuantity = PrescriptionService.GetQuantityUnfulfilledForDrug(d.PZN)
            };
        }

        public MessageDrug CreateDrug(MessageDrug drug)
        {
            Pharmacy.BusinessLayer.Data.Drug d = DrugService.CreateDrug(drug.pzn, drug.name, drug.price, drug.description, drug.drugMinimumAgeYears);
            return Drug(d.PZN.ToString());
        }

        public MessageDrug UpdateDrug(MessageDrug drug, string pzn)
        {
            Pharmacy.BusinessLayer.Data.Drug d = DrugService.GetDrug(Int32.Parse(pzn));
            DrugService.UpdateDrug(d, drug.name, drug.price, drug.description, d.MinimumInventoryLevel, d.OptimalInventoryLevel, d.DrugMinimumAgeYears);
            return Drug(pzn);
        }

        public void CreateDrugs(MessageDrug[] drugs)
        {
            for (int i = 0; i < drugs.Length; i++)
            {
                CreateDrug(drugs[i]);
            }  
        }
        public void UpdateDrugs(MessageDrug[] drugs)
        {
            for (int i = 0; i < drugs.Length; i++)
            {
                UpdateDrug(drugs[i], drugs[i].pzn.ToString());
            }  
        }

        public MessageStatistic GetStatistic()
        {
            MessageStatistic m = new MessageStatistic();
            m.totalNumberOfPrescriptions = Pharmacy.BusinessLayer.Logic.PrescriptionService.TotalNumberOfPrescriptions();

            double? itemsperprescription = Pharmacy.BusinessLayer.Logic.PrescriptionService.AverageNumberOfItemsPerPrescription();
            m.averageNumberOfItemsPerPrescription = itemsperprescription.HasValue ? itemsperprescription.Value : -1;

            TimeSpan? ts = Pharmacy.BusinessLayer.Logic.PrescriptionService.AverageTimeSpanOfFulfilment();
            m.averageTimeSpanOfFulfilment = ts.HasValue ? (long)ts.Value.TotalSeconds : -1;

            return m;
        }

        public MessageStatistic GetStatisticInTimeSpan(string start, string end)
        {
            DateTime start_dt = DateTime.ParseExact(start, "yyyy-MM-dd", null);
            DateTime end_dt = DateTime.ParseExact(end + " 23:59:59", "yyyy-MM-dd HH:mm:ss", null);

            MessageStatistic m = new MessageStatistic();
            m.totalNumberOfPrescriptions = Pharmacy.BusinessLayer.Logic.PrescriptionService.TotalNumberOfPrescriptions(start_dt, end_dt);
            
            double? itemsperprescription  = Pharmacy.BusinessLayer.Logic.PrescriptionService.AverageNumberOfItemsPerPrescription(start_dt, end_dt);
            m.averageNumberOfItemsPerPrescription = itemsperprescription.HasValue ? itemsperprescription.Value : -1;

            TimeSpan? ts = Pharmacy.BusinessLayer.Logic.PrescriptionService.AverageTimeSpanOfFulfilment(start_dt, end_dt);
            m.averageTimeSpanOfFulfilment = ts.HasValue ? (long)ts.Value.TotalSeconds : -1;

            return m;
        }
    }
}