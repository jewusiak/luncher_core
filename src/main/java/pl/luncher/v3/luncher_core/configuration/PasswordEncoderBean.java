package pl.luncher.v3.luncher_core.configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderBean extends BCryptPasswordEncoder implements PasswordEncoder {

}
