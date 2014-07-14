using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace WebLayer.Messages
{
    [DataContract]
    public class MessageStatistic
    {
        [DataMember]
        public int totalNumberOfPrescriptions { get; set; }

        [DataMember]
        public double averageNumberOfItemsPerPrescription { get; set; }

        [DataMember]
        public long averageTimeSpanOfFulfilment { get; set; }

    }
}