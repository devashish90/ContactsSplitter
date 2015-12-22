package org.devashish.contactssplitter.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.FormattedName;

import ezvcard.property.Telephone;
import ezvcard.property.Uid;

public class ContactsSplitterApp {
	private static final String WORKAREA = "/home/devashish/workspace/";
	private static final String OUTFILE = WORKAREA + "addressbook_philips.vcf";
	private static final String INFILE = WORKAREA + "addressbook.vcf";
	
	public static void main(String ar[]) throws Exception {
		List<VCard> vcards = Ezvcard.parse(new File(INFILE)).all();
		List<VCard> out = new ArrayList<VCard>();
		VCard newVcard = null;
		int count = 0;
		for(VCard vcard : vcards) {
			System.out.println("VCARD " + vcard.getUid().getValue());
			List<Telephone> numbers = vcard.getTelephoneNumbers();
			FormattedName name= vcard.getFormattedName();
			int i = 0;
			for(Telephone number : numbers){
				newVcard = new VCard();
				newVcard.setVersion(vcard.getVersion());
				newVcard.setUid(new Uid(UUID.randomUUID().toString()));
				++i;
				++count;
				if(i == 1)
					newVcard.setFormattedName(name);
				else
					newVcard.setFormattedName(name.getValue().replace(" ",  i + " "));
				newVcard.addTelephoneNumber(number);
				newVcard.setStructuredName(vcard.getStructuredName());
				System.out.println(newVcard.getFormattedName().getValue());
				System.out.println(newVcard.getTelephoneNumbers().get(0).getText());
				System.out.println(newVcard.validate(VCardVersion.V3_0));
				out.add(newVcard);
				System.out.println();
			}
		}
		System.out.println(count +" Numbers splitted from " + vcards.size() +" vcard contacts" );
		Ezvcard.write(out).version(VCardVersion.V3_0)
		.go(new File(OUTFILE));
	}
}
