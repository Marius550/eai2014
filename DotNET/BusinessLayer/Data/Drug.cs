
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
    
public partial class Drug
{

    public Drug()
    {

        this.Stock = 0;

        this.MinimumInventoryLevel = 0;

        this.OptimalInventoryLevel = 0;

        this.Events = new HashSet<InventoryEvent>();

    }


    public int PZN { get; set; }

    public string Name { get; set; }

    public string Description { get; set; }

    public int Stock { get; set; }

    public int MinimumInventoryLevel { get; set; }

    public int OptimalInventoryLevel { get; set; }

    public double Price { get; set; }



    public virtual ICollection<InventoryEvent> Events { get; set; }

}

}
