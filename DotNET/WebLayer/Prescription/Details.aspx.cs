using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Pharmacy.BusinessLayer.Data;
using Pharmacy.BusinessLayer.Logic;

namespace WebLayer.Prescription
{
    public partial class Details : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            Pharmacy.BusinessLayer.Data.Prescription p = PrescriptionService.GetPrescription(GetPrescriptionId());
            if (p.State == PrescriptionState.Entry)
            {
                PZNBox.Visible = true;
                AddItemButton.Visible = true;
            }
        }

        private void Page_Error(object sender, EventArgs e)
        {
            Session["LastError"] = Server.GetLastError();
            Server.ClearError();
            Server.Transfer("~/Error.aspx");
        }

        protected void NextState_Command(object sender, CommandEventArgs e)
        {
            if (!Page.IsValid)
                return;

            Int32 id = GetPrescriptionId();
            if (PrescriptionService.GetPrescription(id).State == PrescriptionState.Fulfilling)
            {
                DateTime fulfilmentDate = Util.ParseDateTime(((TextBox)PrescriptionDetailsView.FindControl("FulfilledOnBox")).Text);
                PrescriptionService.UpdateFulfilmentDate(id, fulfilmentDate);
                PrescriptionService.UpdateTotalCost(id, GetTotalPrescriptionCost());
            }
            PrescriptionService.ProceedToNextStage(GetPrescriptionId());
            RedirectToSelf();
        }

        private void RedirectToSelf()
        {
            Response.Redirect("Details.aspx?id=" + GetPrescriptionId());
        }

        private Int32 GetPrescriptionId()
        {
            return Int32.Parse(Request.Params["id"]);
        }

        protected Int32 GetQuantityPending(object pznAsObject)
        {
            Int32 pzn = (Int32)pznAsObject;
            return PrescriptionService.GetQuantityPendingForDrug(pzn);
        }

        protected Int32 GetQuantityRequired(object pznAsObject)
        {
            Int32 pzn = (Int32)pznAsObject;
            return PrescriptionService.GetQuantityRequiredForDrug(pzn);
        }

        protected void PreviousState_Command(object sender, CommandEventArgs e)
        {
            PrescriptionService.ReturnToPreviousState(GetPrescriptionId());
            RedirectToSelf();
        }

        protected void Cancel_Command(object sender, CommandEventArgs e)
        {
            PrescriptionService.Cancel(GetPrescriptionId());
            Response.Redirect("List.aspx");
        }

        protected void Refresh_Command(object sender, CommandEventArgs e)
        {
            Response.Redirect("Details.aspx?id=" + GetPrescriptionId());
        }

        protected void UpdatePrescription_Command(object sender, CommandEventArgs e)
        {
            if (!Page.IsValid)
                return;

            string issuer = ((TextBox)PrescriptionDetailsView.FindControl("IssuerBox")).Text;
            string issueDate = ((TextBox)PrescriptionDetailsView.FindControl("IssuedOnBox")).Text;
            string entryDate = ((TextBox)PrescriptionDetailsView.FindControl("EnteredOnBox")).Text;
            double totalPrice = GetTotalPrescriptionCost();
            PrescriptionService.UpdatePrescription(GetPrescriptionId(), issuer, Util.ParseDate(issueDate), Util.ParseDateTime(entryDate), totalPrice);
            PrescriptionDetailsView.DataBind();
        }

        protected void AddDrug_Command(object sender, CommandEventArgs e)
        {
            if (!Page.IsValid)
                return;

            if (ErrorLabel.Text.Length != 0)
            {
                ErrorLabel.Text = "";
            }

            if (PZNBox.Text.Length == 0)
            {
                ErrorLabel.Text = String.Format("You need to enter the PZN!");
                ErrorLabel.CssClass = "error";
            }
            else if (PZNBox.Text.Length != 0 & verifyDrugMinimumAgeYears())
            {
                PrescriptionService.AddDrug(GetPrescriptionId(), Int32.Parse(PZNBox.Text));
                PZNBox.Text = "";
                ItemsGridView.DataBind();
                PrescriptionDetailsView.DataBind();
            }
            else
            {
                Int64 drugMinimumAgeYears = Pharmacy.BusinessLayer.Logic.DrugService.GetDrug(Int32.Parse(PZNBox.Text)).DrugMinimumAgeYears;

                ErrorLabel.Text = String.Format("Customer is " + getCustomerAge() + ", but needs to be " + drugMinimumAgeYears + " old!");
                ErrorLabel.CssClass = "error";
            }
        }

        protected int getCustomerAge()
        {
            Int32 prescriptionId = Int32.Parse(Request.Params["id"]);
            Int32 customerId = Pharmacy.BusinessLayer.Logic.PrescriptionService.GetPrescription(prescriptionId).CustomerId;
            DateTime customerBirthDate = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(customerId).BirthDate;

            DateTime zeroTime = new DateTime(1, 1, 1);
            System.TimeSpan timespan = DateTime.Today - customerBirthDate;
            int timespanYears = (zeroTime + timespan).Year - 1;

            return timespanYears;
        }

        protected Boolean verifyDrugMinimumAgeYears()
        {
            Int32 prescriptionId = Int32.Parse(Request.Params["id"]);
            Int32 customerId = Pharmacy.BusinessLayer.Logic.PrescriptionService.GetPrescription(prescriptionId).CustomerId;
            DateTime customerBirthDate = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(customerId).BirthDate;
            Int64 drugMinimumAgeYears = Pharmacy.BusinessLayer.Logic.DrugService.GetDrug(Int32.Parse(PZNBox.Text)).DrugMinimumAgeYears;

            DateTime zeroTime = new DateTime(1, 1, 1);
            System.TimeSpan timespan = DateTime.Today - customerBirthDate;
            int timespanYears = (zeroTime + timespan).Year - 1;

            Boolean ageCheck;

            if (timespanYears >= drugMinimumAgeYears)
            {
                ageCheck = true;
            }
            else
            {
                ageCheck = false;
            }
            return ageCheck;
        }

        protected void RemoveDrug_Command(object sender, CommandEventArgs e)
        {
            Int32 itemId = ExtractIntegerArgument(e);
            PrescriptionService.RemoveItem(itemId);
            ItemsGridView.DataBind();
            PrescriptionDetailsView.DataBind();
        }

        private static int ExtractIntegerArgument(CommandEventArgs e)
        {
            return Int32.Parse(e.CommandArgument.ToString());
        }

        protected void FulfilDrug_Command(object sender, CommandEventArgs e)
        {
            Int32 itemId = ExtractIntegerArgument(e);
            PrescriptionService.Fulfil(itemId);
            PrescriptionDetailsView.DataBind();
            ItemsGridView.DataBind();
        }

        protected void ReplenishDrug_Command(object sender, CommandEventArgs e)
        {
            Int32 itemId = ExtractIntegerArgument(e);
            PrescriptionService.Replenish(itemId);
            ItemsGridView.DataBind();
        }

        protected Int32 GetAmountOfItemsInPrescription()
        {
            Int32 prescriptionId = Int32.Parse(Request.Params["id"]);
            Int32 amountOfItemsInPrescription = Pharmacy.BusinessLayer.Logic.PrescriptionService.GetPrescription(prescriptionId).Items.Count;
            return amountOfItemsInPrescription;

        }

        protected double GetTotalPrescriptionCost()
        {
            ICollection<Item> prescriptionItems = new List<Item>(); 
            Int32 prescriptionId = Int32.Parse(Request.Params["id"]);
            prescriptionItems = Pharmacy.BusinessLayer.Logic.PrescriptionService.GetPrescription(prescriptionId).Items;

            double totalPrescriptionPrice = 0;

            foreach (Item i in prescriptionItems)
            {
                totalPrescriptionPrice = totalPrescriptionPrice + Pharmacy.BusinessLayer.Logic.DrugService.GetDrug(i.DrugPZN).Price;
            }
            return totalPrescriptionPrice;
        }

    }
}