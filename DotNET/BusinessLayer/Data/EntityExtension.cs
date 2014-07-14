using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Pharmacy.BusinessLayer.Data
{
    public partial class Item : Fulfillable
    {
        public Boolean Fulfilled()
        {
            return this.State == FulfilmentState.Fulfilled;
        }
    }

    public partial class Drug
    {
        internal void Apply(InventoryEvent inventoryEvent)
        {
            ValidateEvent(inventoryEvent);

            Stock += inventoryEvent.Quantity;
            Events.Add(inventoryEvent);
        }

        private void ValidateEvent(InventoryEvent inventoryEvent)
        {
            if (Stock + inventoryEvent.Quantity < 0)
            {
                throw new ArgumentException("Cannot withdraw below stock of " + Stock);
            }
        }

        internal bool RequiresReplenishment(int quantityPending)
        {
            return Stock + quantityPending < MinimumInventoryLevel;
        }

        internal int ReplenishmentSuggestion(int quantityPending)
        {
            return OptimalInventoryLevel - (Stock + quantityPending);
        }
    }

    public abstract partial class InventoryEvent
    {
        public static InventoryEvent Create(Drug drug, Int32 quantity, DateTime dateOfAction)
        {
            throw new NotImplementedException();
        }

    }

    public partial class WithdrawEvent : InventoryEvent
    {
        public static new InventoryEvent Create(Drug drug, int quantity, DateTime dateOfAction)
        {
            return new WithdrawEvent
            {
                Drug = drug,
                // negate quantity for withdrawal
                Quantity = -quantity,
                DateOfAction = dateOfAction
            };
        }
    }

    public partial class RestockEvent : InventoryEvent
    {
        public static new InventoryEvent Create(Drug drug, int quantity, DateTime dateOfAction)
        {
            return new RestockEvent
            {
                Drug = drug,
                Quantity = quantity,
                DateOfAction = dateOfAction
            };
        }
    }

    public partial class ReplenishEvent : InventoryEvent
    {
        public static new InventoryEvent Create(Drug drug, int quantity, DateTime dateOfAction)
        {
            return new ReplenishEvent
            {
                Drug = drug,
                Quantity = quantity,
                DateOfAction = dateOfAction
            };
        }
    }
}
