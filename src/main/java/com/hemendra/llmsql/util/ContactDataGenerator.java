package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.Lead;
import com.hemendra.llmsql.entity.gen.Account;
import com.hemendra.llmsql.entity.gen.Campaign;
import com.hemendra.llmsql.entity.gen.Contact;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class ContactDataGenerator {

    private static final List<String> CAMPAIGN_IDS = Arrays.asList(
            "0J17YNMGPCMQY", "0J17YNMHPCMQR", "0J17YNMHPCMQS", "0J17YNMHPCMQT",
            "0J17YNMHPCMQV", "0J17YNMHPCMQW", "0J17YNMHPCMQX", "0J17YNMHPCMQY",
            "0J17YNMHTCMQZ", "0J17YNMHTCMR0", "0J17ZWV2TCMTG", "0J17ZWV2TCMTH",
            "0J17ZWV2YCMTS", "0J17ZWV2YCMTT", "0J17ZWV2YCMTV", "0J17ZWV2YCMTW",
            "0J17ZWV2YCMTX", "0J17ZWV2YCMTY", "0J17ZWV2YCMTZ", "0J17ZWV32CMRK",
            "0J17ZWV3JCMTN", "0J17ZWV3JCMTP", "0J17ZWV3JCMTQ", "0J17ZWV3JCMTR",
            "0J17ZWV3JCMTS", "0J17ZWV3JCMTT", "0J17ZWV3JCMTV", "0J17ZWV3JCMTW",
            "0J17ZWV3PCMWZ", "0J17ZWV3PCMX0", "0J17ZWV3PCMX1", "0J17ZWV3PCMX2",
            "0J17ZWV3PCMX3", "0J17ZWV3PCMX4", "0J17ZWV3PCMX5", "0J17ZWV3PCMX6",
            "0J17ZWV3PCMX7", "0J17ZWV3TCMSG", "0J17ZWV3TCMSH", "0J17ZWV3TCMSJ",
            "0J17ZWV3TCMSK", "0J17ZWV3TCMSM", "0J17ZWV3TCMSN", "0J17ZWV3TCMSP",
            "0J17ZWV3TCMSQ", "0J17ZWV3TCMSR", "0J17ZWV3YCMTY", "0J17ZWV3YCMTZ",
            "0J17ZWV3YCMV0", "0J17ZWV3YCMV1", "0J17ZWV3YCMV2", "0J17ZWV3YCMV3",
            "0J17ZWV3YCMV4", "0J17ZWV3YCMV5", "0J17ZWV3YCMV6", "0J17ZWV3YCMV7",
            "0J17ZWV3YCMV8", "0J17ZWV42CMYN", "0J17ZWV42CMYP", "0J17ZWV42CMYQ",
            "0J17ZWV42CMYR", "0J17ZWV42CMYS", "0J17ZWV42CMYT", "0J17ZWV46CMZM",
            "0J17ZWV4ECMZV", "0J17ZWV4ECMZW", "0J17ZWV4ECMZX", "0J17ZWV4JCMSM",
            "0J17ZWV4JCMSN", "0J17ZWV4JCMSP", "0J17ZWV4JCMSQ", "0J17ZWV4JCMSR",
            "0J17ZWV4JCMSS", "0J17ZWV4JCMST", "0J17ZWV4JCMSV", "0J17ZWV4JCMSW",
            "0J17ZWV4JCMSX", "0J17ZWV4JCMSY", "0J17ZWV4JCMSZ", "0J17ZWV4JCMT0",
            "0J17ZWV4PCMSK", "0J17ZWV4PCMSM", "0J17ZWV4PCMSN", "0J17ZWV4PCMSP",
            "0J17ZWV4PCMSQ", "0J17ZWV4PCMSR", "0J17ZWV4PCMSS", "0J17ZWV4PCMST",
            "0J17ZWV4PCMSV", "0J17ZWV4PCMSW", "0J17ZWV4PCMSX", "0J17ZWV4PCMSY",
            "0J17ZWV4PCMSZ", "0J17ZWV4TCMST", "0J17ZWV4TCMSV", "0J17ZWV4TCMSW",
            "0J17ZWV4TCMSX", "0J17ZWV4TCMSY", "0J17ZWV4TCMSZ", "0J17ZWV4TCMT0",
            "0J17ZWV4TCMT1", "0J17ZWV4TCMT2", "0J17ZWV4TCMT3", "0J17ZWV4TCMT4",
            "0J17ZWV4TCMT5", "0J17ZWV4TCMT6", "0J17ZWV4YCMZK", "0J17ZWV4YCMZM",
            "0J17ZWV4YCMZN", "0J17ZWV4YCMZP", "0J17ZWV4YCMZQ", "0J17ZWV4YCMZR",
            "0J17ZWV4YCMZS", "0J17ZWV4YCMZT", "0J17ZWV4YCMZV", "0J17ZWV4YCMZW",
            "0J17ZWV4YCMZX", "0J17ZWV4YCMZY", "0J17ZWV4YCMZZ", "0J17ZWV4YCN00",
            "0J17ZWV4YCN01", "0J17ZWV52CMXK", "0J17ZWV52CMXM", "0J17ZWV52CMXN"
    );

    public Campaign getRandomCampaign() {
        Random random = new Random();
        String randomCampaignId = CAMPAIGN_IDS.get(random.nextInt(CAMPAIGN_IDS.size()));

        Campaign campaign = new Campaign();
        campaign.setId(randomCampaignId);

        return campaign;
    }

    private static final List<String> ACCOUNT_IDS = Arrays.asList(
            "0J0WABNPZW5Z4",
            "0J0XMYC03W7MP",
            "0J187CWQT60QN",
            "0J187CWRE60QR",
            "0J187CWRJ60QH",
            "0J187CWRJ60QJ",
            "0J187CWRJ60QK",
            "0J187CWRJ60QM",
            "0J187CWRJ60QN",
            "0J187CWRJ60QP",
            "0J187CWRP60Q8",
            "0J187CWRP60Q9",
            "0J1899VXP60QW",
            "0J1899VY260QY",
            "0J1899VY660QV",
            "0J1899VY660QW",
            "0J1899VY660QX",
            "0J1899VY660QY",
            "0J1899VY660QZ",
            "0J1899VYA60QS",
            "0J1899VYA60QT",
            "0J1899VYA60QV",
            "0J1899VZY60QG",
            "0J1899VZY60QH",
            "0J1899VZY60QJ",
            "0J1899VZY60QK",
            "0J1899VZY60QM",
            "0J1899W0260QG",
            "0J1899W0260QH",
            "0J1899W0260QJ",
            "0J1899W0260QK",
            "0J1899W0260QM",
            "0J1899W0260QN",
            "0J1899W0260QP",
            "0J1899W0260QQ",
            "0J1899W0660QQ",
            "0J1899W0660QR",
            "0J1899W0660QS",
            "0J1899W0660QT",
            "0J1899W0660QV",
            "0J1899W0660QW",
            "0J1899W0660QX",
            "0J1899W0660QY",
            "0J1899W0A60QR",
            "0J1899W0A60QS",
            "0J1899W0A60QT",
            "0J1899W0A60QV",
            "0J1899W0A60QW",
            "0J1899W0A60QX",
            "0J1899W0A60QY",
            "0J1899W0A60QZ",
            "0J1899W0A60R0",
            "0J1899W0E60QM",
            "0J1899W0E60QN",
            "0J1899W0E60QP",
            "0J1899W0E60QQ",
            "0J1899W0E60QR",
            "0J1899W0E60QS",
            "0J1899W0E60QT",
            "0J1899W0E60QV",
            "0J1899W0J60QY",
            "0J1899W0J60QZ",
            "0J1899W0J60R0",
            "0J1899W0J60R1",
            "0J1899W0J60R2",
            "0J1899W0J60R3",
            "0J1899W1T60QQ",
            "0J1899W1Y60QV",
            "0J1899W1Y60QW",
            "0J1899W1Y60QX",
            "0J1899W1Y60QY",
            "0J1899W1Y60QZ",
            "0J1899W1Y60R0",
            "0J1899W1Y60R1",
            "0J1899W1Y60R2",
            "0J1899W1Y60R3",
            "0J1899W2260QG",
            "0J1899W2260QH",
            "0J1899W2260QJ",
            "0J1899W2260QK",
            "0J1899W2260QM",
            "0J1899W2260QN",
            "0J1899W2260QP",
            "0J1899W2260QQ",
            "0J1899W2660QQ",
            "0J1899W2660QR",
            "0J1899W2660QS",
            "0J1899W2660QT",
            "0J1899W2660QV",
            "0J1899W2660QW",
            "0J1899W2660QX",
            "0J1899W2660QY",
            "0J1899W2660QZ",
            "0J1899W2A60QG",
            "0J1899W2A60QH",
            "0J1899W2A60QJ",
            "0J1899W2A60QK",
            "0J1899W2A60QM"
    );

    public Account getRandomAccount() {
        Random random = new Random();
        String randomAccountId = ACCOUNT_IDS.get(random.nextInt(ACCOUNT_IDS.size()));

        Account account = new Account();
        account.setId(randomAccountId);

        return account;
    }

    List<String> LEAD_IDS = Arrays.asList(
            "0J104EHKW4GWM", "0J104EHKW4GWN", "0J104EHKW4GWP", "0J104EHKW4GWQ", "0J104EHKW4GWR",
            "0J104EHKW4GWS", "0J104EHKW4GWT", "0J104EHKW4GWV", "0J104EHKW4GWW", "0J104EHKW4GWX",
            "0J104EHKW4GWY", "0J104EHKW4GWZ", "0J104EHKW4GX0", "0J104EHKW4GX1", "0J104EHKW4GX2",
            "0J104EHKW4GX3", "0J104EHKW4GX4", "0J104EHKW4GX5", "0J104EHKW4GX6", "0J104EHKW4GX7",
            "0J104EHKW4GX8", "0J104EHKW4GX9", "0J104EHKW4GXA", "0J104EHKW4GXB", "0J104EHKW4GXC",
            "0J104EHKW4GXD", "0J104EHKW4GXE", "0J104EHKW4GXF", "0J104EHKW4GXG", "0J104EHKW4GXH",
            "0J104EHKW4GXJ", "0J104EHKW4GXK", "0J104EHKW4GXM", "0J104EHKW4GXN", "0J104EHKW4GXP",
            "0J104EHKW4GXQ", "0J104EHKW4GXR", "0J104EHKW4GXS", "0J104EHKW4GXT", "0J104EHKW4GXV",
            "0J104EHKW4GXW", "0J104EHKW4GXX", "0J104EHKW4GXY", "0J104EHKW4GXZ", "0J104EHM04GWR",
            "0J104EHM04GWS", "0J104EHM04GWT", "0J104EHM04GWV", "0J104EHM04GWW", "0J104EHM04GWX",
            "0J104EHM04GWY", "0J104EHM04GWZ", "0J104EHM04GX0", "0J104EHM04GX1", "0J104EHM04GX2",
            "0J104EHM04GX3", "0J104EHM04GX4", "0J104EHM04GX5", "0J104EHM04GX6", "0J104EHM04GX7",
            "0J104EHM04GX8", "0J104EHM04GX9", "0J104EHM04GXA", "0J104EHM04GXB", "0J104EHM04GXC",
            "0J104EHM04GXD", "0J104EHM04GXE", "0J104EHM04GXF", "0J104EHM04GXG", "0J104EHM04GXH",
            "0J104EHM04GXJ", "0J104EHM04GXK", "0J104EHM04GXM", "0J104EHM04GXN", "0J104EHM04GXP",
            "0J104EHM04GXQ", "0J104EHM04GXR", "0J104EHM04GXS", "0J104EHM04GXT", "0J104EHM04GY0",
            "0J104EHM04GY1", "0J104EHM04GY2", "0J104EHM04GY3", "0J104EHM04GY4", "0J104EHM04GY5",
            "0J104EHM04GY6", "0J104EHM04GY7", "0J104EHM04GY8", "0J104EHM04GY9", "0J104EHM04GYA",
            "0J104EHM04GYB", "0J104EHM04GYC", "0J104EHM04GYD", "0J104EHM04GYE", "0J104EHM04GYF",
            "0J104EHM04GYG", "0J104EHM04GYH", "0J104EHM04GYJ", "0J104EHM04GYK", "0J104EHM04GYM",
            "0J104EHM04GYN", "0J104EHM04GYP", "0J104EHM04GYQ", "0J104EHM04GYR", "0J104EHM04GYS",
            "0J104EHM04GYT", "0J104EHM04GYV", "0J104EHM04GYW", "0J104EHM04GYX", "0J104EHM04GYY",
            "0J104EHM04GYZ", "0J104EHM04GZ0", "0J104EHM44GS0", "0J104EHM44GS1", "0J104EHM44GS2",
            "0J104EHM44GS3", "0J104EHM44GS4", "0J104EHM44GS5", "0J104EHM44GS6", "0J104EHM44GS7",
            "0J104EHM44GS8", "0J104EHM44GS9", "0J104EHM44GSA", "0J104EHM44GSB", "0J104EHM44GSC",
            "0J104EHM44GSD", "0J104EHM44GSE", "0J104EHM44GSF", "0J104EHM44GSG", "0J104EHM44GSH",
            "0J104EHM44GSJ", "0J104EHM44GSK", "0J104EHM44GSM", "0J104EHM44GSN", "0J104EHM44GSP",
            "0J104EHM44GSQ", "0J104EHM44GSR", "0J104EHM44GSS", "0J104EHM44GST", "0J104EHM44GSV",
            "0J104EHM44GSW", "0J104EHM44GSX", "0J104EHM44GSY", "0J104EHM44GSZ", "0J104EHM44GT0",
            "0J104EHM44GT1", "0J104EHM44GT2", "0J104EHM44GT3", "0J104EHM44GT4", "0J104EHM44GT5",
            "0J104EHM44GT6", "0J104EHM44GT7", "0J104EHM44GT8", "0J104EHM44GT9", "0J104EHM44GTA",
            "0J104EHM44GTB", "0J104EHM44GTC", "0J104EHM44GTD", "0J104EHM44GTE", "0J104EHM44GTF",
            "0J104EHM44GTG", "0J104EHM44GTH", "0J104EHM44GTJ", "0J104EHM44GTK", "0J104EHM44GTM",
            "0J104EHM44GTN", "0J104EHM44GTP", "0J104EHM44GTQ", "0J104EHM44GTR", "0J104EHM44GTS",
            "0J104EHM44GTT", "0J104EHM44GTV", "0J104EHM44GTW", "0J104EHM44GTX", "0J104EHM44GTY",
            "0J104EHM44GTZ", "0J104EHM44GV0", "0J104EHM44GV1", "0J104EHM44GV2", "0J104EHM44GV3",
            "0J104EHM44GV4", "0J104EHM44GV5", "0J104EHM44GV6", "0J104EHM44GV7", "0J104EHM44GV8",
            "0J104EHM44GV9");

    public Lead getRandomLead() {
        Random random = new Random();
        String randomAccountId = LEAD_IDS.get(random.nextInt(LEAD_IDS.size()));

        Lead lead = new Lead();
        lead.setId(randomAccountId);

        return lead;
    }

    List<String> LEAD_SOURCES = List.of("Web", "Phone Inquiry", "Partner Referral", "Purchased List", "Other");

    // Salutation options
    List<String> SALUTATIONS = List.of("Mr.", "Mrs.", "Miss", "Ms.", "Dr.", "Prof.", "Sir", "Madam", "Sir/Madam");


    public List<Contact> generateContacts(int count) {
        List<Contact> contacts = new ArrayList<>();
        Faker faker = new Faker();
        Random random = new Random();
        // Lead source options




        for (int i = 0; i < count; i++) {
            Contact contact = new Contact();

            // Primary key ID left null (to be generated by JPA)
            contact.setFirstName(faker.name().firstName());
            contact.setLastName(faker.name().lastName());
            contact.setEmailAddress(faker.internet().emailAddress());
            contact.setPhoneNumber(faker.phoneNumber().phoneNumber());
            contact.setCreatedBy(faker.name().username());
            contact.setCreatedOn(OffsetDateTime.now());
            contact.setOrganisationId(faker.idNumber().valid());
            contact.setUpdatedBy(faker.name().username());
            contact.setUpdatedOn(OffsetDateTime.now().minusDays(random.nextInt(30))); // Random update within the past month
            contact.setAccountIsInactive(random.nextBoolean());
            contact.setAccountRole(faker.job().position());
            contact.setCity(faker.address().city());
            contact.setCountry(faker.address().country());
            contact.setPostalCode(faker.address().zipCode());
            contact.setState(faker.address().state());
            contact.setStreet(faker.address().streetAddress());
            contact.setAssistant(faker.name().fullName());
            contact.setAsstPhone(Long.parseLong(faker.phoneNumber().subscriberNumber(10)));
            contact.setBirthDate(OffsetDateTime.now().minusYears(20 + random.nextInt(30))); // Random age between 20 and 50
            contact.setDepartment(faker.company().industry());
            contact.setDescription(faker.lorem().paragraph());
            contact.setDoNotCall(random.nextBoolean());
            contact.setEmailAddress(faker.internet().emailAddress());
            contact.setFax(faker.phoneNumber().subscriberNumber(10));
            contact.setLeadSource(LEAD_SOURCES.get(random.nextInt(LEAD_SOURCES.size()))); // Random lead source
            contact.setMobile(Long.parseLong(faker.phoneNumber().subscriberNumber(10)));
            contact.setName(faker.name().fullName());
            contact.setOpportunityRole(faker.job().title());
            contact.setPhoneNumber(faker.phoneNumber().subscriberNumber(10));
            contact.setSalutationName(SALUTATIONS.get(random.nextInt(SALUTATIONS.size()))); // Random salutation
            contact.setTitle(faker.job().title());
            contact.setIsActive(true); // Default to active
            contact.setIsDeleted(false); // Default to not deleted

            // Mocked relationships
            contact.setAccount(getRandomAccount());

            // Mock Campaign ID
            contact.setCampaign(getRandomCampaign());

            contacts.add(contact);
        }

        return contacts;
    }
}
