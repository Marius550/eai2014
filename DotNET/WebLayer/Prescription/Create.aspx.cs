using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebLayer.Prescription
{
    public partial class Create : System.Web.UI.Page
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

        protected void SubmitBtn_Click(object sender, EventArgs e)
        {
            if (!Page.IsValid)
                return;

            Pharmacy.BusinessLayer.Data.Prescription p = Pharmacy.BusinessLayer.Logic.CustomerService.CreatePrescription(GetCustomerId(), IssuerBox.Text);
            // TODO redirect to prescription
            Response.Redirect(String.Format("Details.aspx?id={0}", p.Id));
        }

        private Int32 GetCustomerId()
        {
            return Int32.Parse(Request.Params["id"]);
        }
    }
}