﻿<%@ Page Title="C.Sharpe - Prescription List" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true"
    CodeBehind="List.aspx.cs" Inherits="WebLayer.Prescription.List" %>
<asp:Content ID="Content" ContentPlaceHolderID="ContentHolder" runat="server">
    <form id="PrescriptionListForm" runat="server">
        <asp:ObjectDataSource ID="PrescriptionDataSource" runat="server"
            SelectMethod="GetAllPrescriptionsInState" TypeName="Pharmacy.BusinessLayer.Logic.PrescriptionService"
            DataObjectTypeName="Pharmacy.BusinessLayer.Data.Prescription">
            <SelectParameters>
                <asp:QueryStringParameter Name="filter" QueryStringField="filter" Type="String" />
            </SelectParameters>
        </asp:ObjectDataSource>
        <asp:HyperLink NavigateUrl="List.aspx" Text="All" runat="server" /> |
        <asp:HyperLink NavigateUrl="List.aspx?filter=Entry" Text="Entry" runat="server" /> |
        <asp:HyperLink NavigateUrl="List.aspx?filter=Checking" Text="Checking" runat="server" /> |
        <asp:HyperLink NavigateUrl="List.aspx?filter=Fulfilling" Text="Fulfilling" runat="server" /> |
        <asp:HyperLink NavigateUrl="List.aspx?filter=Fulfilled" Text="Fulfilled" runat="server" />
        <asp:GridView ID="PrescriptionsGridView" DataSourceID="PrescriptionDataSource" runat="server" AutoGenerateColumns="false">
            <Columns>
                <asp:BoundField DataField="ID" HeaderText="ID" />
                <asp:TemplateField HeaderText="Customer">
                    <ItemTemplate>
                        <asp:HyperLink runat="server" ID="CustomerDetailsLink"
                            NavigateUrl='<%# String.Format("~/Customer/Details.aspx?id={0}", Eval("Customer.Id")) %>'
                            Text='<%# Eval("Customer.Name") %>' />
                    </ItemTemplate>
                </asp:TemplateField>
                <asp:BoundField DataField="IssuingPhysician" HeaderText="Issuer" />
                <asp:BoundField DataField="TotalPrice" HeaderText="Total price" DataFormatString="{0:C2}"/>
                <asp:BoundField DataField="State" HeaderText="State" />
                <asp:HyperLinkField DataNavigateUrlFields="ID" DataNavigateUrlFormatString="Details.aspx?id={0}" Text="Details" />
            </Columns>
            <EmptyDataTemplate>
                No prescriptions (in the filtered state) yet.
            </EmptyDataTemplate>
        </asp:GridView>
        <asp:Button ID="TestBtn" runat="server" Text="Exam practice" onclick="TestBtn_Click" />
    </form>
    <br/>
        <b><% Response.Write("Total prescription price: " + Pharmacy.BusinessLayer.Logic.PrescriptionService.GetTotalPriceOfAllPrescriptions() + ",00 Euro"); %></b>
    <br/>
</asp:Content>
