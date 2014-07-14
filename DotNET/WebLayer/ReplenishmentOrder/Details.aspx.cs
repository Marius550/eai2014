using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Globalization;
using Pharmacy.BusinessLayer.Data;
using Pharmacy.BusinessLayer.Logic;

namespace WebLayer.ReplenishmentOrder
{
    public partial class Details : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        private void Page_Error(object sender, EventArgs e)
        {
            Session["LastError"] = Server.GetLastError();
            Server.ClearError();
            Server.Transfer("~/Error.aspx");
        }

        protected void NextStateButton_Command(object sender, CommandEventArgs e)
        {
            if (!Page.IsValid)
                return;

            Pharmacy.BusinessLayer.Data.ReplenishmentOrder order = OrderService.GetOrder(GetOrderId());
            if (order.State == OrderState.Posting)
            {
                string expectedDelivery = ((TextBox)OrderDetailsView.FindControl("ExpectedDeliveryBox")).Text;
                OrderService.UpdateExpectedDeliveryDate(order.Id, parseDateTime(expectedDelivery));
            }

            if (order.State == OrderState.Ordered)
            {
                string actualDelivery = ((TextBox)OrderDetailsView.FindControl("ActualDeliveryBox")).Text;
                OrderService.UpdateActualDeliveryDate(order.Id, parseDateTime(actualDelivery));
            }
            OrderService.ProceedToNextState(order.Id);
            // display updated data
            OrderDetailsView.DataBind();
        }

        private Int32 GetOrderId()
        {
            return Int32.Parse(Request.Params["id"]);
        }

        private static DateTime parseDateTime(string dateAsString)
        {
            return Util.ParseDateTime(dateAsString);
        }

        protected void Cancel_Command(object sender, CommandEventArgs e)
        {
            OrderService.Cancel(GetOrderId());
            // display updated data
            OrderDetailsView.DataBind();
        }
    }
}