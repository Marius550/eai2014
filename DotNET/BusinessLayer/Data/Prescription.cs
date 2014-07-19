//------------------------------------------------------------------------------
// <auto-generated>
//    Dieser Code wurde aus einer Vorlage generiert.
//
//    Manuelle Änderungen an dieser Datei führen möglicherweise zu unerwartetem Verhalten Ihrer Anwendung.
//    Manuelle Änderungen an dieser Datei werden überschrieben, wenn der Code neu generiert wird.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Pharmacy.BusinessLayer.Data
{
    using System;
    using System.Collections.Generic;
    
    public partial class Prescription
    {
        public Prescription()
        {
            this.Items = new HashSet<Item>();
        }
    
        public int Id { get; set; }
        public Pharmacy.BusinessLayer.Data.PrescriptionState State { get; set; }
        public string IssuingPhysician { get; set; }
        public System.DateTime IssueDate { get; set; }
        public System.DateTime EntryDate { get; set; }
        public Nullable<System.DateTime> FulfilmentDate { get; set; }
        public int CustomerId { get; set; }
        public string Diagnosis { get; set; }
    
        public virtual ICollection<Item> Items { get; set; }
        public virtual Customer Customer { get; set; }
    }
}
