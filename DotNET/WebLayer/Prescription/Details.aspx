<%@ Page Title="C.Sharpe - Prescription Details" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true"
    CodeBehind="Details.aspx.cs" Inherits="WebLayer.Prescription.Details" %>
<%@ Import Namespace="Pharmacy.BusinessLayer.Data" %>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentHolder" runat="server">
    <form id="PrescriptionDetailsForm" runat="server">
        <asp:ObjectDataSource ID="PrescriptionDataSource" runat="server"
            SelectMethod="GetPrescription"
            TypeName="Pharmacy.BusinessLayer.Logic.PrescriptionService"
            DataObjectTypeName="Pharmacy.BusinessLayer.Data.Prescription">
            <SelectParameters>
                <asp:QueryStringParameter Name="Id" QueryStringField="id" Type="Int32" />
            </SelectParameters>
        </asp:ObjectDataSource>
        <asp:DetailsView ID="PrescriptionDetailsView" runat="server" AutoGenerateRows="false"
            BackColor="White" BorderColor="#CCCCCC" BorderStyle="None" BorderWidth="1px" CellPadding="4"
            ForeColor="Black" GridLines="Horizontal" Height="50px" 
            DataSourceID="PrescriptionDataSource">
            <Fields>
                <asp:BoundField DataField="Id" HeaderText="Prescription" ReadOnly="true" />
                <asp:TemplateField HeaderText="Customer">
                    <ItemTemplate>
                        <asp:HyperLink runat="server" ID="CustomerDetailsLink"
                            NavigateUrl='<%# String.Format("~/Customer/Details.aspx?id={0}", Eval("Customer.Id")) %>'
                            Text='<%# Eval("Customer.Name") %>' />
                    </ItemTemplate>
                </asp:TemplateField>
                <asp:BoundField DataField="State" HeaderText="State" />
                <asp:TemplateField HeaderText="Issuer">
                    <ItemTemplate>
                        <asp:Label ID="IssuerLabel" runat="server"
                            Text='<%# Eval("IssuingPhysician") %>' 
                            Visible='<%# ((PrescriptionState)Eval("State")) != PrescriptionState.Entry %>' />
                        <asp:TextBox ID="IssuerBox" TextMode="SingleLine" runat="server"
                            Text='<%# Eval("IssuingPhysician") %>'
                            Visible='<%# ((PrescriptionState)Eval("State")) == PrescriptionState.Entry %>' />
                        <asp:RequiredFieldValidator ID="RequiredIssuer" ControlToValidate="IssuerBox"
                            EnableClientScript="false" runat="server">
                            Issuer required
                        </asp:RequiredFieldValidator>
                    </ItemTemplate>
                </asp:TemplateField>
                <asp:TemplateField HeaderText="Issued on">
                    <ItemTemplate>
                        <asp:Label ID="IssuedOnLabel" runat="server"
                            Text='<%# Eval("IssueDate", "{0:dd.MM.yyyy}") %>' 
                            Visible='<%# ((PrescriptionState)Eval("State")) != PrescriptionState.Entry %>' />
                        <asp:TextBox ID="IssuedOnBox" TextMode="DateTime" runat="server"
                            Text='<%# Eval("IssueDate", "{0:dd.MM.yyyy}") %>'
                            Visible='<%# ((PrescriptionState)Eval("State")) == PrescriptionState.Entry %>' />
                        <asp:RequiredFieldValidator ID="RequiredIssued" ControlToValidate="IssuedOnBox"
                            EnableClientScript="false" runat="server">
                            Issued on required
                        </asp:RequiredFieldValidator>
                        <asp:RegularExpressionValidator ID="ValidIssuedOn" ControlToValidate="IssuedOnBox"
                            EnableClientScript="false" runat="server"
                            ValidationExpression="\d{2}.\d{2}.\d{4}">
                            Invalid date (e.g. 24.12.2013)
                        </asp:RegularExpressionValidator>
                    </ItemTemplate>
                </asp:TemplateField>
                <asp:TemplateField HeaderText="Entered on">
                    <ItemTemplate>
                        <asp:Label ID="EnteredOnLabel" runat="server"
                            Text='<%# Eval("EntryDate", "{0:dd.MM.yyyy HH:mm}") %>' 
                            Visible='<%# ((PrescriptionState)Eval("State")) != PrescriptionState.Entry %>' />
                        <asp:TextBox ID="EnteredOnBox" TextMode="DateTime" runat="server"
                            Text='<%# Eval("EntryDate", "{0:dd.MM.yyyy HH:mm}") %>'
                            Visible='<%# ((PrescriptionState)Eval("State")) == PrescriptionState.Entry %>' />
                        <asp:RequiredFieldValidator ID="RequiredEnteredOn" ControlToValidate="EnteredOnBox"
                            EnableClientScript="false" runat="server">
                            Entered on required
                        </asp:RequiredFieldValidator>
                        <asp:RegularExpressionValidator ID="ValidEnteredOn" ControlToValidate="EnteredOnBox"
                            EnableClientScript="false" runat="server"
                            ValidationExpression="\d{2}.\d{2}.\d{4} \d{2}:\d{2}">
                            Invalid date (e.g. 24.12.2013 18:32)
                        </asp:RegularExpressionValidator>
                    </ItemTemplate>
                </asp:TemplateField>
                <asp:TemplateField HeaderText="Fulfilled on">
                    <ItemTemplate>
                        <asp:Label runat="server"
                            Text="Not fulfilled yet." 
                            Visible='<%# ((PrescriptionState)Eval("State")) != PrescriptionState.Fulfilled &&
                                        !(((PrescriptionState)Eval("State")) == PrescriptionState.Fulfilling &&
                                           ((PrescriptionState)Eval("State")).Proceedable((ICollection<Item>)Eval("Items"))) %>' />
                        <asp:Label ID="FulfilledOnLabel" runat="server"
                            Text='<%# Eval("FulfilmentDate", "{0:dd.MM.yyyy HH:mm}") %>' 
                            Visible='<%# ((PrescriptionState)Eval("State")) == PrescriptionState.Fulfilled %>' />
                        <asp:TextBox ID="FulfilledOnBox" TextMode="DateTime" runat="server"
                            Text='<%# String.Format("{0:dd.MM.yyyy HH:mm}", DateTime.Now) %>'
                            Visible='<%# ((PrescriptionState)Eval("State")) == PrescriptionState.Fulfilling && 
                                        ((PrescriptionState)Eval("State")).Proceedable((ICollection<Item>)Eval("Items")) %>' />
                        <asp:RequiredFieldValidator ID="RequiredFulfilledOn" ControlToValidate="FulfilledOnBox"
                            EnableClientScript="false" runat="server">
                            Fulfilled on required
                        </asp:RequiredFieldValidator>
                        <asp:RegularExpressionValidator ID="ValidFulfilledOn" ControlToValidate="FulfilledOnBox"
                            EnableClientScript="false" runat="server"
                            ValidationExpression="\d{2}.\d{2}.\d{4} \d{2}:\d{2}">
                            Invalid date (e.g. 24.12.2013 18:32)
                        </asp:RegularExpressionValidator>
                    </ItemTemplate>
                </asp:TemplateField>
                <asp:TemplateField>
                    <ItemTemplate>
                        <asp:Button ID="UpdatePrescriptionButton" runat="server" OnCommand="UpdatePrescription_Command"
                            Text="Update prescription" Visible='<%# ((PrescriptionState)Eval("State")) == PrescriptionState.Entry %>' />
                        <asp:Button ID="PreviousStateButton" runat="server" OnCommand="PreviousState_Command"
                            Text='<%# "Return to " + ((PrescriptionState)Eval("State")).Previous() %>'
                            Enabled='<%# ((PrescriptionState)Eval("State")).Reversible() %>'
                            Visible='<%# ((PrescriptionState)Eval("State")).Reversible() %>' />
                        <asp:Button ID="NextStateButton" runat="server" OnCommand="NextState_Command"
                            Text='<%# "Proceed to " + ((PrescriptionState)Eval("State")).Next() %>'
                            Enabled='<%# ((PrescriptionState)Eval("State")).Proceedable((ICollection<Item>)Eval("Items")) %>'
                            Visible='<%# ((PrescriptionState)Eval("State")).Proceedable((ICollection<Item>)Eval("Items")) %>' />
                        <asp:Button ID="CancelButton" runat="server" OnCommand="Cancel_Command" Text="Cancel"
                            Enabled='<%# ((PrescriptionState)Eval("State")).Cancellable() %>'
                            Visible='<%# ((PrescriptionState)Eval("State")).Cancellable() %>' />
                    </ItemTemplate>
                </asp:TemplateField>
            </Fields>
        </asp:DetailsView>
        <h3>Prescribed drugs</h3>
        <asp:ObjectDataSource ID="ItemsDataSource" runat="server"
            SelectMethod="GetItemsForPrescription"
            TypeName="Pharmacy.BusinessLayer.Logic.PrescriptionService"
            DataObjectTypeName="Pharmacy.BusinessLayer.Data.Item">
            <SelectParameters>
                <asp:QueryStringParameter Name="Id" QueryStringField="id" Type="Int32" />
            </SelectParameters>
        </asp:ObjectDataSource>
        <asp:GridView ID="ItemsGridView" DataSourceID="ItemsDataSource" runat="server" AutoGenerateColumns="false">
            <Columns>
                <asp:TemplateField HeaderText="PZN">
                    <ItemTemplate>
                        <asp:HyperLink runat="server" ID="DrugLink" 
                            NavigateUrl='<%# String.Format("~/Drug/Details.aspx?pzn={0}", Eval("PrescribedDrug.PZN")) %>'
                            Text='<%# Eval("PrescribedDrug.PZN") %>' />
                    </ItemTemplate>
                </asp:TemplateField>
                <asp:BoundField DataField="PrescribedDrug.Name" HeaderText="Name" />
                <asp:BoundField DataField="State" HeaderText="State" />
                <asp:BoundField DataField="PrescribedDrug.Stock" HeaderText="In Stock" />
                <asp:TemplateField HeaderText="Pending">
                    <ItemTemplate>
                        <asp:Label runat="server"
                            Text='<%# GetQuantityPending(Eval("PrescribedDrug.PZN")) %>' />
                    </ItemTemplate>
                </asp:TemplateField>
                <asp:TemplateField HeaderText="Required">
                    <ItemTemplate>
                        <asp:Label runat="server"
                            Text='<%# GetQuantityRequired(Eval("PrescribedDrug.PZN")) %>' />
                    </ItemTemplate>
                </asp:TemplateField>
                <asp:TemplateField>
                    <ItemTemplate>
                        <asp:Button OnCommand="RemoveDrug_Command" CommandArgument='<%# Eval("ID") %>'
                            Text="Remove" runat="server"
                            Visible='<%# ((PrescriptionState)Eval("Prescription.State")) == PrescriptionState.Entry %>' />
                        <asp:Button OnCommand="FulfilDrug_Command" CommandArgument='<%# Eval("ID") %>'
                            Text="Fulfil" runat="server"
                            Visible='<%# (FulfilmentState)Eval("State") == FulfilmentState.Unfulfilled && ((PrescriptionState)Eval("Prescription.State")) == PrescriptionState.Fulfilling %>'
                            Enabled='<%# (int)Eval("PrescribedDrug.Stock") > 0 %>' />
                        <asp:Button OnCommand="ReplenishDrug_Command" CommandArgument='<%# Eval("ID") %>'
                            Text="Replenish" runat="server"
                            Visible='<%# GetQuantityRequired(Eval("PrescribedDrug.PZN")) > 0 && ((PrescriptionState)Eval("Prescription.State")) == PrescriptionState.Fulfilling %>' />
                    </ItemTemplate>
                </asp:TemplateField>
            </Columns>
            <EmptyDataTemplate>
                No prescribed drugs added yet.
            </EmptyDataTemplate>
        </asp:GridView>
        <asp:TextBox ID="PZNBox" runat="server" TextMode="Number" Visible="false" />
        <asp:RangeValidator ID="PZNNumberValidator" runat="server"
            ControlToValidate="PZNBox" ErrorMessage="RangeValidator"
            Type="Integer" MinimumValue="1" MaximumValue="99999999"
            EnableClientScript="false">
            <span class="error">Enter valid PZN (8 digits)</span>
        </asp:RangeValidator>
        <asp:Button ID="AddItemButton" runat="server" Text="Add Drug" Visible="false"
            OnCommand="AddDrug_Command" />
    </form>
</asp:Content>
