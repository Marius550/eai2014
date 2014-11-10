using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace WebLayer.Messages
{
    [DataContract]
    public class MessageCustomer
    {
        [DataMember]
        public int id { get; set; }

        [DataMember]
        public string name { get; set; }

        [DataMember]
        public string telephoneNumber { get; set; }

        [DataMember]
        public string address { get; set; }

        [DataMember]
        public string email { get; set; }

        [DataMember]
        public double prescriptionBill { get; set; }

        [DataMember]
        public System.DateTime birthDate { get; set; }

        public MessageCustomer()
        {
        }

        public MessageCustomer(Pharmacy.BusinessLayer.Data.Customer customer)
        {
            Fill(customer);
        }

        public void Fill(Pharmacy.BusinessLayer.Data.Customer customer)
        {
            id = customer.Id;
            name = customer.Name;
            telephoneNumber = customer.TelephoneNumber;
            address = customer.Address;
            email = customer.Email;
            prescriptionBill = customer.PrescriptionBill;
            birthDate = customer.BirthDate;
        }
    }
}