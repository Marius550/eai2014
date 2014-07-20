using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Pharmacy.BusinessLayer.Data;

namespace Pharmacy.BusinessLayer.Logic
{
    public static class CustomerService
    {
        public static Customer CreateCustomer(string name, string telephoneNumber, string address)
        {
            Util.ConvertEmptyToNull(ref address);

            Customer customer = new Customer
            {
                Name = name,
                TelephoneNumber = telephoneNumber,
                Address = address
            };
            return CreateCustomer(customer);
        }

        private static Customer CreateCustomer(Customer newCustomer)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                var count = (from c in db.CustomerSet where c.Name == newCustomer.Name select c).Count();
                if (count > 0)
                {
                    throw new ArgumentException(String.Format("Customer with name {0} already exists!", newCustomer.Name));
                }
                db.CustomerSet.Add(newCustomer);
                db.SaveChanges();
                return newCustomer;
            }
        }

        public static Customer GetCustomer(Int32 id)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                return GetCustomer(id, db);
            }
        }

        internal static Customer GetCustomer(Int32 id, PharmacyContainer db)
        {
            Customer result = (from c in db.CustomerSet where c.Id == id select c).FirstOrDefault();
            if (result == default(Customer))
            {
                throw new ArgumentException(String.Format("Customer with id {0} not found", id));
            }
            return result;
        }

        public static Customer UpdateCustomer(Customer customer)
        {
            return UpdateCustomer(customer, customer.TelephoneNumber, customer.Address);
        }

        public static Customer UpdateCustomer(Customer customer, string telephoneNumber, string address)
        {
            Util.ConvertEmptyToNull(ref address);

            using (PharmacyContainer db = new PharmacyContainer())
            {
                Customer attachedCustomer = GetCustomer(customer.Id, db);
                attachedCustomer.TelephoneNumber = telephoneNumber;
                attachedCustomer.Address = address;
                db.SaveChanges();
                return attachedCustomer;
            }
        }

        public static Prescription CreatePrescription(int customerId, string issuer)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Customer currentCustomer = GetCustomer(customerId, db);
                Prescription newPrescription = new Prescription
                {
                    Customer = currentCustomer,
                    IssuingPhysician = issuer,
                    IssueDate = DateTime.Now,
                    EntryDate = DateTime.Now
                };
                currentCustomer.Prescriptions.Add(newPrescription);
                db.SaveChanges();
                return newPrescription;
            }
        }
    }
}
