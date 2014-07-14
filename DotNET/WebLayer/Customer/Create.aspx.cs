using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebLayer.Customer
{
    public partial class Create : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void SubmitBtn_Click(object sender, EventArgs e)
        {
            if (!Page.IsValid)
                return;
            try
            {
                Pharmacy.BusinessLayer.Data.Customer result =
                    Pharmacy.BusinessLayer.Logic.CustomerService.CreateCustomer(NameBox.Text, TelephoneNumberBox.Text, AddressBox.Text);
                ResultLabel.Text = String.Format("Customer '{0}' created.", result.Name);
                ResultLabel.CssClass = "success";
                NameBox.Text = "";
                TelephoneNumberBox.Text = "";
                AddressBox.Text = "";
            }
            catch (ArgumentException ex)
            {
                ResultLabel.Text = String.Format("Customer not created: {0}", ex.Message);
                ResultLabel.CssClass = "error";
            }
        }

    }
}