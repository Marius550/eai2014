
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
    
public partial class ReplenishmentOrder
{

    public ReplenishmentOrder()
    {

        this.Positions = new HashSet<Position>();

    }


    public int Id { get; set; }

    public Pharmacy.BusinessLayer.Data.OrderState State { get; set; }

    public Nullable<System.DateTime> ExpectedDelivery { get; set; }

    public Nullable<System.DateTime> ActualDelivery { get; set; }



    public virtual ICollection<Position> Positions { get; set; }

}

}
