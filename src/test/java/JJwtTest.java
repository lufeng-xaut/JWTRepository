import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import org.junit.Test;

import java.util.Date;

public class JJwtTest {
    private final String secret = "123456";
    private final Clock clock = DefaultClock.INSTANCE;

    @Test
    public void getTokenByHS256() {
        // JWT指定的七个默认字段,语法如下：
//        Jwts.builder()
//                .setHeader(new HashMap<String, Object>()) // 可不设置，默认格式是{"alg":"HS256"}
//                .setId(null)
//                .setIssuer(null)
//                .setSubject(null)
//                .setAudience(null)
//                .setExpiration(null) // 过期时间,非必须，可以是永久Token
//                .setNotBefore(null)
//                .setIssuedAt(null) //设置签发时间
//                .setClaims(new HashMap<String, Object>()) // 自定义信息
//                .signWith(SignatureAlgorithm.HS256, secret) // 设置签名算法
//                .compact(); // 开始压缩为xxxxx.yyyyy.zzzzz 格式的jwt token

        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("1")
                .setSubject("zhangsan")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secret)
                .setExpiration(new Date(new Date().getTime() + 3000)) // 过期时间为3秒钟
                .claim("userId", "1") //自定义信息
                .claim("userName", "zhangsan");
        String token = jwtBuilder.compact();
        System.out.println(token);

        // JWT解析
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        System.out.println(claims);
        System.out.println(claims.get("userName"));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!validateToken(token)) {
            System.out.println("身份已过期，请重新认证!");
        }
    }

    public Boolean validateToken(final String token) {
        return !this.isTokenExpired(token);
    }

    private Boolean isTokenExpired(final String token) {
        // 注:超时解析时会抛异常,所以要try-catch
        Date expiration = null;
        try {
            expiration = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
        } catch (Exception e) {
            return true;
        }
        return expiration.before(this.clock.now());
    }

    public String refreshToken(final String token) {
        final Date createdDate = this.clock.now();
        final Date expirationDate = new Date(createdDate.getTime() + 60000);
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, this.secret).compact();
    }

    @Test
    public void getTokenByRSA() throws Exception {
//
//        // RSA keyPair Generator
//        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        /*
//         * 长度 至少 1024, 建议 2048
//         */
//        final int keySize = 2048;
//        keyPairGenerator.initialize(keySize);
//
//        final KeyPair keyPair = keyPairGenerator.genKeyPair();
//
//        final PrivateKey privateKey = keyPair.getPrivate();
//
//        // gen id_token
//        final Date exp = DateUtils.addMinutes(new Date(), 5);
//        final JwtBuilder jwtBuilder = Jwts.builder().setId("jti").setSubject("sub").setExpiration(exp).signWith(SignatureAlgorithm.RS512, privateKey);
//        final String idToken = jwtBuilder.compact();
//
//
//        assertNotNull(idToken);
//        System.out.println(idToken);
//
//
//        // verify
//
//        final PublicKey publicKey = keyPair.getPublic();
////        final Jwt jwt = Jwts.parser().parse(idToken);
//        final JwtParser parser = Jwts.parser();
//        final Jwt jwt = parser.setSigningKey(publicKey).parse(idToken);
//
//        assertNotNull(jwt);
//        System.out.println(jwt.getHeader());
//        System.out.println(jwt.getBody());
    }


}