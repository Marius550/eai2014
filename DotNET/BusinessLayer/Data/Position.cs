
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
    
public partial class Position
{

    public int Id { get; set; }

    public int Quantity { get; set; }

    public int DrugPZN { get; set; }

    public int ReplenishmentOrderId { get; set; }



    public virtual Drug Drug { get; set; }

    public virtual ReplenishmentOrder Order { get; set; }

}

}
