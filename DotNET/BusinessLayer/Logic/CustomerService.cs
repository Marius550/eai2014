using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Pharmacy.BusinessLayer.Data;
using System.Globalization;

namespace Pharmacy.BusinessLayer.Logic
{
    public static class CustomerService
    {
        //Exam practice
        public static Customer CreateCustomer(string name, string telephoneNumber, string address, string email, DateTime birthDate)
        {
            Util.ConvertEmptyToNull(ref address);

            Customer newCustomer = new Customer();
            newCustomer.Name = name;
            newCustomer.TelephoneNumber = telephoneNumber;
            newCustomer.Address = address;
            newCustomer.Email = email;
            newCustomer.BirthDate = birthDate;

            using (PharmacyContainer db = new PharmacyContainer())
            {
                var count = (from c in db.CustomerSet where c.Name == name select c).Count();

                if (count > 0)
                {
                    throw new ArgumentException(String.Format("Customer with name {0} already exists!", newCustomer.Name));
                }

                db.CustomerSet.Add(newCustomer);
                db.SaveChanges();
                return newCustomer;
            }
        }

        /*
        public static Customer CreateCustomer(string name, string telephoneNumber, string address, string email, DateTime birthDate)
        {
            Util.ConvertEmptyToNull(ref address);

            Customer customer = new Customer
            {
                Name = name,
                TelephoneNumber = telephoneNumber,
                Address = address,
                Email = email,
                BirthDate = birthDate
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
         * 
         * //Exam practice
        public static Customer GetCustomer(Int32 id)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Customer result = (from c in db.CustomerSet where c.Id == id select c).FirstOrDefault();

                if (result == default(Customer)) 
                {
                    throw new ArgumentException(String.Format("Customer with id {0} not found", id));
                }
                return result;
            }
        }
         * */

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


        public static Customer UpdateCustomer(Customer customer, string telephoneNumber, string address, string email, DateTime birthDate)
        {
            Util.ConvertEmptyToNull(ref address);

            using (PharmacyContainer db = new PharmacyContainer())
            {
                Customer attachedCustomer = GetCustomer(customer.Id, db);
                attachedCustomer.TelephoneNumber = telephoneNumber;
                attachedCustomer.Address = address;
                attachedCustomer.Email = email;
                attachedCustomer.BirthDate = birthDate;
                System.Diagnostics.Debug.Write("Arrived at UpdateCustomer(...)" + "\n");
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

        /* //Exam practice
        public static Prescription CreatePrescription(int customerId, string issuer) {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Customer currentCustomer = GetCustomer(customerId, db);
                Prescription newPrescription = new Prescription();

                newPrescription.Customer = currentCustomer;
                newPrescription.IssuingPhysician = issuer;
                newPrescription.IssueDate = DateTime.Now;
                newPrescription.EntryDate = DateTime.Now;

                currentCustomer.Prescriptions.Add(newPrescription);
                db.SaveChanges();
                return newPrescription;
            }
        }
         */

        public static double getCustomerPrescriptionBill(int Id)
        {
            ICollection<Pharmacy.BusinessLayer.Data.Prescription> customerPrescriptions = new List<Pharmacy.BusinessLayer.Data.Prescription>();
            customerPrescriptions = Pharmacy.BusinessLayer.Logic.PrescriptionService.GetAllPrescriptionsForCustomer(Id);

            double PrescriptionBill = 0;

            foreach (Pharmacy.BusinessLayer.Data.Prescription i in customerPrescriptions)
            {
                PrescriptionBill = PrescriptionBill + Pharmacy.BusinessLayer.Logic.PrescriptionService.GetPrescription(i.Id).TotalPrice;
            }
            return PrescriptionBill;
        }

        public static ICollection<Customer> GetAllCustomers()
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                return db.CustomerSet.ToList();
            }
            
        }

        public static void UpdateCustomerPrescriptionBill(Int32 Id, double prescriptionBill)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Customer attachedCustomer = GetCustomer(Id, db);
                attachedCustomer.PrescriptionBill = prescriptionBill;
                db.SaveChanges();
            }
        }

    }
}
