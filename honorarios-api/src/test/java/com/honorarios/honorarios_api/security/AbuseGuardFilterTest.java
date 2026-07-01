package com.honorarios.honorarios_api.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbuseGuardFilterTest {

    private MockHttpServletResponse run(AbuseGuardFilter f, MockHttpServletRequest req) throws Exception {
        MockHttpServletResponse res = new MockHttpServletResponse();
        f.doFilter(req, res, new MockFilterChain());
        return res;
    }

    private MockHttpServletRequest authReq() {
        MockHttpServletRequest r = new MockHttpServletRequest("POST", "/api/auth/login");
        r.setRemoteAddr("1.2.3.4");
        return r;
    }

    @Test
    void bloqueiaAposCapacidadeNoMesmoIp() throws Exception {
        AbuseGuardFilter f = new AbuseGuardFilter();
        for (int i = 0; i < 10; i++) {
            assertEquals(200, run(f, authReq()).getStatus(), "tentativa " + i + " deveria passar");
        }
        assertEquals(429, run(f, authReq()).getStatus(), "11a tentativa deveria ser limitada");
    }

    @Test
    void ipsDiferentesNaoCompartilhamLimite() throws Exception {
        AbuseGuardFilter f = new AbuseGuardFilter();
        for (int i = 0; i < 11; i++) run(f, authReq());          // 1.2.3.4 estourado
        MockHttpServletRequest outro = authReq();
        outro.setRemoteAddr("9.9.9.9");
        assertEquals(200, run(f, outro).getStatus());
    }

    @Test
    void rejeitaCorpoGrande() throws Exception {
        MockHttpServletRequest r = authReq();
        r.setContentType("application/json");
        r.setContent(new byte[64 * 1024 + 1]);
        assertEquals(413, run(new AbuseGuardFilter(), r).getStatus());
    }

    @Test
    void naoLimitaEndpointsNormais() throws Exception {
        AbuseGuardFilter f = new AbuseGuardFilter();
        MockHttpServletRequest r = new MockHttpServletRequest("GET", "/api/clientes");
        r.setRemoteAddr("1.2.3.4");
        for (int i = 0; i < 20; i++) assertEquals(200, run(f, r).getStatus());
    }
}
