using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Pharmacy.BusinessLayer.Data
{
    public enum PrescriptionState
    {
        Entry, Checking, Fulfilling, Fulfilled
    }

    public static class PrescriptionStateExtension
    {
        public static PrescriptionState Next(this PrescriptionState currentState)
        {
            switch (currentState)
            {
                case PrescriptionState.Entry:
                    return PrescriptionState.Checking;
                case PrescriptionState.Checking:
                    return PrescriptionState.Fulfilling;
                case PrescriptionState.Fulfilling:
                    return PrescriptionState.Fulfilled;
                default:
                    // to have a reasonable default behaviour (instead of throwing an exception)
                    return currentState;
            }
        }

        public static Boolean Proceedable(this PrescriptionState currentState, ICollection<Item> items)
        {
            switch (currentState)
            {
                case PrescriptionState.Entry:
                    return items.Any();
                case PrescriptionState.Checking:
                    return true;
                case PrescriptionState.Fulfilling:
                    return items.All(f => f.Fulfilled());
                default:
                    return false;
            }
        }

        public static PrescriptionState Previous(this PrescriptionState currentState)
        {
            switch (currentState)
            {
                case PrescriptionState.Checking:
                    return PrescriptionState.Entry;
                default:
                    return currentState;
            }
        }

        public static Boolean Reversible(this PrescriptionState state)
        {
            return state == PrescriptionState.Checking;
        }

        public static Boolean Cancellable(this PrescriptionState state)
        {
            return state == PrescriptionState.Entry || state == PrescriptionState.Checking;
        }
    }
}