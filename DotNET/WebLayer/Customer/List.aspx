﻿<%@ Page Title="C.Sharpe - Customer List" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="List.aspx.cs" Inherits="WebLayer.Customer.List" %>
<asp:Content ID="Content" ContentPlaceHolderID="ContentHolder" runat="server">
    <form runat="server">
        <asp:EntityDataSource ID="CustomerDataSource" runat="server"
            ConnectionString="name=PharmacyContainer" DefaultContainerName="PharmacyContainer"
            EntitySetName="CustomerSet" EntityTypeFilter="Customer">
        </asp:EntityDataSource>
        <asp:HyperLink NavigateUrl="Create.aspx" Text="Create/add new customer" runat="server" />
        <asp:GridView ID="CustomerGridView" runat="server" DataSourceID="CustomerDataSource" AutoGenerateColumns="false">
            <Columns>
                <asp:BoundField DataField="Id" HeaderText="Id" />
                <asp:BoundField DataField="Name" HeaderText="Name" />
                <asp:BoundField DataField="TelephoneNumber" HeaderText="Telephone number" />
                <asp:BoundField DataField="Address" HeaderText="Address" />
                <asp:BoundField DataField="BirthDate" HeaderText="Birth date" dataformatstring="{0:dd.MM.yyyy}" />
                <asp:BoundField DataField="PrescriptionBill" HeaderText="Prescription bill" DataFormatString="{0:C2}"/>
                <asp:HyperLinkField DataNavigateUrlFields="Id" DataNavigateUrlFormatString="Details.aspx?id={0}"
                    Text="Details" />
            </Columns>
            <EmptyDataTemplate>
                No customers yet.
            </EmptyDataTemplate>
        </asp:GridView>
    </form>
</asp:Content>
