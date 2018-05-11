package eu.cehj.cdb2.hub.service.search;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import eu.cehj.cdb2.common.dto.BailiffDTO;
import eu.cehj.cdb2.hub.service.soap.france.Etude;
import eu.cehj.cdb2.hub.service.soap.france.ListeEtudeByInsee;
import eu.cehj.cdb2.hub.service.soap.france.ListeEtudeByInseeResponse;

public class FranceSearchService extends WebServiceGatewaySupport implements LocalWSSearchService {

    @Value("${cdb.france.soap.action}")
    private String soapAction;

    @Override
    public List<BailiffDTO> sendQuery(final String countryCode, final MultiValueMap<String, String> params){
        final ListeEtudeByInsee req = new ListeEtudeByInsee();
        final String postalCode = params.getFirst("postalCode");
        if (isNotBlank(postalCode)) {
            req.setInsee(postalCode);
        }
        final String name = params.getFirst("name");
        if(isNotBlank(name)) {
            req.setNom(name);
        }
        final ListeEtudeByInseeResponse resp = (ListeEtudeByInseeResponse) this.getWebServiceTemplate().marshalSendAndReceive(req,
                new SoapActionCallback(this.soapAction));
        final List<Etude> rawBailiffs = resp.getListeEtudeByInseeResult().getEtude();
        return rawBailiffs.stream().map(this::convertEtudeToDTO).collect(Collectors.toList());
    }

    private BailiffDTO convertEtudeToDTO(final Etude e) {
        final BailiffDTO dto = new BailiffDTO();
        dto.setName(e.getNom());
        dto.setAddress1(e.getAds1());
        dto.setAddress2(e.getAds2());
        dto.setPostalCode(e.getCp());
        dto.setCity(e.getVille());
        dto.setEmail(e.getEm1());
        dto.setPhone(e.getTel());
        dto.setFax(e.getFax());
        dto.setWebSite(e.getWeb());
        dto.setOpenHours(e.getHor());
        return dto;
    }

}
