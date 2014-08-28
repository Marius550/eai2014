using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Pharmacy.BusinessLayer.Data;

namespace WebLayer.Prescription
{
    public partial class List : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void getCustomerPrescriptionBill()
        {
            /*
            Pharmacy.BusinessLayer.Logic.PrescriptionService.GetAllPrescriptionsForCustomer(1);

            ICollection<Drug> customerPrescriptions = new List<Item>();

            //Int32 prescriptionId = Int32.Parse(Request.Params["id"]);
            //prescriptionItems = Pharmacy.BusinessLayer.Logic.PrescriptionService.GetPrescription(prescriptionId).Items;

            double totalPrescriptionPrice = 0;

            foreach (Item i in prescriptionItems)
            {
                totalPrescriptionPrice = totalPrescriptionPrice + Pharmacy.BusinessLayer.Logic.DrugService.GetDrug(i.DrugPZN).Price;
            }
            return totalPrescriptionPrice;
             * */
        }
    }
}