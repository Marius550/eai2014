using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Pharmacy.BusinessLayer.Data
{
    public enum OrderState
    {
        Open, Posting, Ordered, Finished, Cancelled
    }

    public static class OrderStateExtension
    {
        public static OrderState Next(this OrderState currentState)
        {
            switch (currentState)
            {
                case OrderState.Open:
                    return OrderState.Posting;
                case OrderState.Posting:
                    return OrderState.Ordered;
                case OrderState.Ordered:
                    return OrderState.Finished;
                default:
                    // to have a reasonable default behaviour (instead of throwing an exception)
                    return currentState;
            }
        }

        public static Boolean Proceedable(this OrderState state)
        {
            return !(state == OrderState.Finished || state == OrderState.Cancelled);
        }

        public static OrderState Cancel(this OrderState state)
        {
            if (state != OrderState.Posting)
                throw new ArgumentException("Order can only be cancelled in state " + OrderState.Posting);
            return OrderState.Cancelled;
        }

        public static Boolean Cancellable(this OrderState state)
        {
            return state == OrderState.Posting;
        }
    }
}
