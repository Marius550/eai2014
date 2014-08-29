using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebLayer.Customer
{
    public partial class List : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            ICollection<Pharmacy.BusinessLayer.Data.Customer> allCustomers = new List<Pharmacy.BusinessLayer.Data.Customer>();
            allCustomers = Pharmacy.BusinessLayer.Logic.CustomerService.GetAllCustomers();

            foreach (Pharmacy.BusinessLayer.Data.Customer i in allCustomers)
            {
                double prescriptionBill = Pharmacy.BusinessLayer.Logic.CustomerService.getCustomerPrescriptionBill(i.Id);
                Pharmacy.BusinessLayer.Logic.CustomerService.UpdateCustomerPrescriptionBill(i.Id, prescriptionBill);
            }

        }
    }
}