import org.moqui.entity.EntityValue

// CREATE PARTY
party = ec.entity.makeValue("party.Party")
if (!party.partyId) party.setSequencedIdPrimary()
party.partyTypeEnumId = partyTypeEnumId
party.createdDate = ec.user.nowTimestamp
party = party.create()

// from tutorial
//def tutorial = ec.entity.makeValue("tutorial.Tutorial")
//tutorial.setFields(context, true, null, null)
//if (!tutorial.tutorialId) tutorial.setSequencedIdPrimary()
//tutorial.create()

def partyId = party.partyId

if (partyTypeEnumId != "PERSON" && partyTypeEnumId != "PARTY_GROUP") {
    ec.message.addError("Party Type is invalid")
    return
}


//  CREATE SUBTYPE
if (partyTypeEnumId == "PERSON") {

    if (!firstName || !lastName) {
        ec.message.addError("First Name and Last Name are required for Person")
        return
    }

    ec.entity.makeValue("party.Person")
            .set("partyId", partyId)
            .set("firstName", firstName)
            .set("lastName", lastName)
            .set("dateOfBirth", dateOfBirth)
            .create()

}

if (partyTypeEnumId == "PARTY_GROUP") {

    if (!groupName) {
        ec.message.addError("Group Name is required for Party Group")
        return
    }

    ec.entity.makeValue("party.PartyGroup")
            .set("partyId", partyId)
            .set("groupName", groupName)
            .set("description", description)
            .create()
}

// HELPER METHOD FOR CONTACT
def createContactMech = { String typeId, String value ->

    cm = ec.entity.makeValue("party.ContactMech")
    if (!cm.contactMechId) cm.setSequencedIdPrimary()
    cm.contactMechTypeId = typeId
    cm.infoString = value
    cm.createdDate = ec.user.nowTimestamp
    cm = cm.create()

    ec.entity.makeValue("party.PartyContactMech")
            .set("partyId", partyId)
            .set("contactMechId", cm.contactMechId)
            .set("fromDate", ec.user.nowTimestamp)
            .create()
}

//  CREATE CONTACT MECHS
if (emailAddress) {
    createContactMech("EMAIL", emailAddress)
}

if (phoneNumber) {
    createContactMech("TELECOM_NUMBER", phoneNumber)
}

if (postalAddress) {
    createContactMech("POSTAL_ADDRESS", postalAddress)
}

// 5. OUTPUT
context.partyId = partyId
responseMessage = "Party with full details created successfully!"
