using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace WebLayer.Messages
{
    [DataContract]
    public class MessageDrug
    {
        [DataMember]
        public int pzn { get; set; }

        [DataMember]
        public string name { get; set; }

        public double price { get; set; }

        [DataMember]
        public string description { get; set; }

        [DataMember]
        public int minimumInventoryLevel { get; set; }

        [DataMember]
        public int optimalInventoryLevel { get; set; }

        [DataMember]
        public int stock { get; set; }

        [DataMember]
        public int pendingQuantity { get; set; }

        [DataMember]
        public int unfulfilledQuantity { get; set; }

        public MessageDrug()
        {
        }

        public MessageDrug(Pharmacy.BusinessLayer.Data.Drug drug)
        {
            Fill(drug);
        }

        public void Fill(Pharmacy.BusinessLayer.Data.Drug drug)
        {
            pzn = drug.PZN;
            name = drug.Name;
            description = drug.Description;
            minimumInventoryLevel = drug.MinimumInventoryLevel;
            optimalInventoryLevel = drug.OptimalInventoryLevel;
            stock = drug.Stock;
        }
    }
}