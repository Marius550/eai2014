using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Pharmacy.BusinessLayer.Data;

namespace Pharmacy.BusinessLayer.Logic
{
    public static class PrescriptionService
    {
        public static Prescription GetPrescription(Int32 id)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                return GetPrescription(id, db);
            }
        }

        internal static Prescription GetPrescription(Int32 id, PharmacyContainer db)
        {
            Prescription result = (from p in db.PrescriptionSet.Include("Customer").Include("Items") where p.Id == id select p).FirstOrDefault();
            
            if (result == default(Prescription))
                throw new ArgumentException(String.Format("Prescription with id {0} not found", id));

            return result;
        }

        public static ICollection<Prescription> GetAllPrescriptionsInState(String filter)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                PrescriptionState? filteredState = ParseFromString(filter);

                var all = db.PrescriptionSet.Include("Customer");
                if (filteredState == null) { 
                    return all.ToList();
                } else {
                    return all.Where(p => p.State == filteredState).ToList();
                }
            }
        }

        public static PrescriptionState? ParseFromString(String state)
        {
            try {
                return (PrescriptionState)Enum.Parse(typeof(PrescriptionState), state);
            } catch (ArgumentException) {
                return null;
            }
        }

        public static ICollection<Prescription> GetAllPrescriptionsForCustomer(Int32 customerId)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                return (from p in db.PrescriptionSet
                        where p.CustomerId == customerId
                        select p).ToList();
            }
        }

        public static ICollection<Item> GetItemsForPrescription(Int32 id)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                return (from i in db.ItemSet.Include("PrescribedDrug").Include("Prescription")
                        where i.Prescription.Id == id
                        select i).ToList();
            }
        }

        public static int GetQuantityRequiredForDrug(int pzn)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                return GetQuantityRequiredForDrug(pzn, db);
            }
        }
        
        internal static int GetQuantityRequiredForDrug(Int32 pzn, PharmacyContainer db)
        {
            Drug d = DrugService.GetDrug(pzn, db);
            int optimalInventoryLevel = d.OptimalInventoryLevel;
            int quantityUnfulfilled = GetQuantityUnfulfilledForDrug(pzn, db); // Now using the method GetQuantityUnfulfilledForDrug
            int currentStock = d.Stock;
            int quantityPending = GetQuantityPendingForDrug(pzn);
            int quantityRequired = (optimalInventoryLevel + quantityUnfulfilled) - (currentStock + quantityPending);
            return Math.Max(0, quantityRequired);
        }

        /// <summary>
        /// Method to get the unfulfilled quantity for a drug.
        /// </summary>
        /// <param name="pzn">The pzn</param>
        /// <returns>The quantity</returns>
        public static int GetQuantityUnfulfilledForDrug(Int32 pzn)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                return GetQuantityUnfulfilledForDrug(pzn, db);
            }
        }

        /// <summary>
        /// Method to get the unfulfilled quantity for a drug.
        /// </summary>
        /// <param name="pzn">The pzn</param>
        /// <param name="db">Database</param>
        /// <returns>The quantitiy</returns>
        internal static int GetQuantityUnfulfilledForDrug(Int32 pzn, PharmacyContainer db)
        {
            return (from i in db.ItemSet
                    where i.PrescribedDrug.PZN == pzn
                    && i.State == FulfilmentState.Unfulfilled
                    && (i.Prescription.State == PrescriptionState.Checking
                        || i.Prescription.State == PrescriptionState.Fulfilling)
                    select i).Count();
        }

        public static int GetQuantityPendingForDrug(int pzn)
        {
            return OrderService.GetPendingPositionsForDrug(pzn).Aggregate(0, (sum, p) => sum + p.Quantity);
        }

        public static void Cancel(Int32 id)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Prescription p = GetPrescription(id, db);
                p.Items.ToList().ForEach(i => db.ItemSet.Remove(i));
                db.Entry(p).State = System.Data.EntityState.Deleted;
                db.SaveChanges();
            }
        }

        public static void UpdatePrescription(Int32 id, string issuer, DateTime issueDate, DateTime entryDate)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Prescription p = GetPrescription(id, db);
                p.IssuingPhysician = issuer;
                p.IssueDate = issueDate;
                p.EntryDate = entryDate;
                db.SaveChanges();
            }
        }

        public static void ProceedToNextStage(Int32 id)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Prescription p = GetPrescription(id, db);
                p.State = p.State.Next();
                db.SaveChanges();
            }
        }

        public static void ReturnToPreviousState(Int32 id)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Prescription p = GetPrescription(id, db);
                p.State = p.State.Previous();
                db.SaveChanges();
            }
        }

        public static void AddDrug(Int32 id, Int32 pzn)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Prescription p = GetPrescription(id, db);
                Drug d = DrugService.GetDrug(pzn, db);
                
                if (p.Items.Any(i => i.PrescribedDrug.PZN == pzn))
                    throw new ArgumentException(String.Format("Drug with pzn {0} already contained in prescription with id {1}", pzn, id));

                p.Items.Add(new Item { Prescription = p, PrescribedDrug = d });
                db.SaveChanges();
            }
        }

        public static void RemoveItem(int itemId)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Item item = (from i in db.ItemSet where i.Id == itemId select i).SingleOrDefault();

                if (item == default(Item))
                    throw new ArgumentException(String.Format("Item with id {0} not found", itemId));

                db.ItemSet.Remove(item);
                db.SaveChanges();
            }
        }

        public static void Fulfil(Int32 itemId)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Item i = GetItem(itemId, db);
                Int32 pzn = i.PrescribedDrug.PZN;

                if (i.Fulfilled())
                    throw new ArgumentException(String.Format("Item for drug with {0} already fulfilled", pzn));

                DrugService.Withdraw(pzn, 1, DateTime.Now);
                i.State = FulfilmentState.Fulfilled;
                db.SaveChanges();
            }
        }

        internal static Item GetItem(Int32 itemId, PharmacyContainer db)
        {
            return (from i in db.ItemSet.Include("PrescribedDrug") where i.Id == itemId select i).Single();
        }

        public static void Replenish(Int32 itemId)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Item i = GetItem(itemId, db);
                Int32 pzn = i.PrescribedDrug.PZN;
                OrderService.InitiateReplenishment(pzn, GetQuantityRequiredForDrug(pzn), db);
            }
        }

        public static void UpdateFulfilmentDate(int id, DateTime fulfilmentDate)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                Prescription p = GetPrescription(id, db);
                p.FulfilmentDate = fulfilmentDate;
                db.SaveChanges();
            }
        }

        /// <summary>
        /// Returns the total number of prescriptions
        /// </summary>
        /// <returns>Total number of prescriptions</returns>
        public static int TotalNumberOfPrescriptions()
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                return db.PrescriptionSet.Count();
            }
        }

        /// <summary>
        /// Returns the total number of prescriptions in a given time span
        /// </summary>
        /// <param name="start">The start date for the time span</param>
        /// <param name="end">The end date for the time span</param>
        /// <returns>Number of prescriptions in a given time span</returns>
        public static int TotalNumberOfPrescriptions(DateTime start, DateTime end)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                return db.PrescriptionSet.Where(p => p.EntryDate >= start && p.EntryDate <= end).Count();
            }
        }

        /// <summary>
        /// Returns the average number of items per prescription
        /// </summary>
        /// <returns>The average number of items per prescription</returns>
        public static double? AverageNumberOfItemsPerPrescription()
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                var itemcount = db.PrescriptionSet.Include("Items").Where(p => p.Items.Count() > 0).Select(p => p.Items.Count());

                if (itemcount.Any())
                    return itemcount.Average();
                return null;
            }
        }

        /// <summary>
        /// Returns the average number of items per prescription in a given time span
        /// </summary>
        /// <param name="start">The start date of the time span</param>
        /// <param name="end">The end date of the time span</param>
        /// <returns>The average number of items per prescription in the given time span</returns>
        public static double? AverageNumberOfItemsPerPrescription(DateTime start, DateTime end)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                var itemcount = db.PrescriptionSet.Include("Items")
                    .Where(p => p.EntryDate >= start && p.EntryDate <= end && p.Items.Count() > 0)
                    .Select(p => p.Items.Count());

                if (itemcount.Any())
                    return itemcount.Average();
                return null;
            }
        }

        /// <summary>
        /// Returns the average time span of filfilment
        /// </summary>
        /// <returns>The average time span of fulfilment in seconds</returns>
        public static TimeSpan? AverageTimeSpanOfFulfilment()
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                double? diff = db.PrescriptionSet
                    .Where(p => p.State == PrescriptionState.Fulfilled)
                    .Select(p => System.Data.Objects.SqlClient.SqlFunctions.DateDiff("ss", p.EntryDate, p.FulfilmentDate))
                    .Where(t => t.HasValue)
                    .Average();

                if (diff.HasValue)
                    return TimeSpan.FromSeconds(diff.Value);
                else
                    return null;
            }
        }

        /// <summary>
        /// Returns the average time span of filfilment in a given time span
        /// </summary>
        /// <param name="start">The start date of the time span</param>
        /// <param name="end">The end date of the time span</param>
        /// <returns>The average time span of fulfilment in seconds in the given time span</returns>
        public static TimeSpan? AverageTimeSpanOfFulfilment(DateTime start, DateTime end)
        {
            using (PharmacyContainer db = new PharmacyContainer())
            {
                double? diff = db.PrescriptionSet
                    .Where(p => p.EntryDate >= start && p.EntryDate <= end && p.State == PrescriptionState.Fulfilled)
                    .Select(p => System.Data.Objects.SqlClient.SqlFunctions.DateDiff("ss", p.EntryDate, p.FulfilmentDate))
                    .Where(t => t.HasValue)
                    .Average();

                if (diff.HasValue)
                    return TimeSpan.FromSeconds(diff.Value);
                else
                    return null;
            }
        }
 
    }
}
