
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
    
public partial class Customer
{

    public Customer()
    {

        this.Prescriptions = new HashSet<Prescription>();

    }


    public int Id { get; set; }

    public string Name { get; set; }

    public string TelephoneNumber { get; set; }

    public string Address { get; set; }

    public string Email { get; set; }

    public double PrescriptionBill { get; set; }

    public System.DateTime BirthDate { get; set; }



    public virtual ICollection<Prescription> Prescriptions { get; set; }

}

}
