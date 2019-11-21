package pl.nogacz.forum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.dto.authentication.AuthenticationRequestDto;
import pl.nogacz.forum.dto.authentication.RegisterRequestDto;
import pl.nogacz.forum.dto.post.AddCommentRequestDto;
import pl.nogacz.forum.dto.post.AddTopicRequestDto;
import pl.nogacz.forum.dto.post.EditCommentRequestDto;
import pl.nogacz.forum.dto.user.UserChangePasswordDto;
import pl.nogacz.forum.util.html.clean.HtmlClean;
import pl.nogacz.forum.util.html.clean.HtmlCleanType;

@Service
@AllArgsConstructor
public class CleanService {
    private HtmlClean clean;

    public String cleanStringPlainText(final String string) {
        return this.clean.cleanText(string, HtmlCleanType.PLAIN_TEXT);
    }

    public AddTopicRequestDto cleanAddTopicRequestDto(final AddTopicRequestDto addTopicRequestDto) {
        addTopicRequestDto.setText(
                this.clean.cleanText(addTopicRequestDto.getText(), HtmlCleanType.BASIC_HTML)
        );

        addTopicRequestDto.setTag(
                this.clean.cleanText(addTopicRequestDto.getTag(), HtmlCleanType.PLAIN_TEXT)
        );

        addTopicRequestDto.setTitle(
                this.clean.cleanText(addTopicRequestDto.getTitle(), HtmlCleanType.PLAIN_TEXT)
        );

        return addTopicRequestDto;
    }

    public AddCommentRequestDto cleanAddCommentRequestDto(final AddCommentRequestDto addCommentRequestDto) {
        addCommentRequestDto.setText(
                this.clean.cleanText(addCommentRequestDto.getText(), HtmlCleanType.BASIC_HTML)
        );

        return addCommentRequestDto;
    }

    public EditCommentRequestDto cleanEditCommentRequestDto(final EditCommentRequestDto editCommentRequestDto) {
        editCommentRequestDto.setText(
                this.clean.cleanText(editCommentRequestDto.getText(), HtmlCleanType.BASIC_HTML)
        );

        return editCommentRequestDto;
    }

    public UserChangePasswordDto cleanUserChangePasswordDto(final UserChangePasswordDto userChangePasswordDto) {
        userChangePasswordDto.setNewPassword(
                this.clean.cleanText(userChangePasswordDto.getNewPassword(), HtmlCleanType.PLAIN_TEXT)
        );

        userChangePasswordDto.setOldPassword(
                this.clean.cleanText(userChangePasswordDto.getOldPassword(), HtmlCleanType.PLAIN_TEXT)
        );

        return userChangePasswordDto;
    }

    public RegisterRequestDto cleanRegisterRequestDto(final RegisterRequestDto registerRequestDto) {
        registerRequestDto.setEmail(
                this.clean.cleanText(registerRequestDto.getEmail(), HtmlCleanType.PLAIN_TEXT)
        );

        registerRequestDto.setPassword(
                this.clean.cleanText(registerRequestDto.getPassword(), HtmlCleanType.PLAIN_TEXT)
        );

        registerRequestDto.setUsername(
                this.clean.cleanText(registerRequestDto.getUsername(), HtmlCleanType.PLAIN_TEXT)
        );

        return registerRequestDto;
    }

    public AuthenticationRequestDto cleanAuthenticationRequestDto(final AuthenticationRequestDto authenticationRequestDto) {
        authenticationRequestDto.setPassword(
                this.clean.cleanText(authenticationRequestDto.getPassword(), HtmlCleanType.PLAIN_TEXT)
        );

        authenticationRequestDto.setUsername(
                this.clean.cleanText(authenticationRequestDto.getUsername(), HtmlCleanType.PLAIN_TEXT)
        );

        return authenticationRequestDto;
    }
}
