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

        //Exam practice
        protected void TestBtn_Click(object sender, EventArgs e)
        {
            Pharmacy.BusinessLayer.Logic.PrescriptionService.GetAllPrescriptionsInStateVoid("Fulfilled");
            Pharmacy.BusinessLayer.Logic.PrescriptionService.GetTotalPriceOfAllPrescriptions();
        }
    }
}