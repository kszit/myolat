/**
 * OLAT - Online Learning and Training<br>
 * http://www.olat.org
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Copyright (c) frentix GmbH<br>
 * http://www.frentix.com<br>
 * <p>
 */
package org.olat.presentation.webfeed.blog;

import java.util.Calendar;
import java.util.Date;

import javax.management.timer.Timer;

import org.olat.data.commons.vfs.VFSContainer;
import org.olat.lms.webfeed.Feed;
import org.olat.lms.webfeed.FeedManager;
import org.olat.lms.webfeed.Item;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.components.form.flexible.FormItem;
import org.olat.presentation.framework.core.components.form.flexible.FormItemContainer;
import org.olat.presentation.framework.core.components.form.flexible.elements.DateChooser;
import org.olat.presentation.framework.core.components.form.flexible.elements.FormLink;
import org.olat.presentation.framework.core.components.form.flexible.elements.IntegerElement;
import org.olat.presentation.framework.core.components.form.flexible.elements.RichTextElement;
import org.olat.presentation.framework.core.components.form.flexible.elements.TextElement;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormBasicController;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormEvent;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormLayoutContainer;
import org.olat.presentation.framework.core.components.form.flexible.impl.elements.richText.RichTextConfiguration;
import org.olat.presentation.framework.core.components.link.Link;
import org.olat.presentation.framework.core.control.Controller;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.presentation.framework.core.translator.Translator;
import org.olat.system.event.Event;

/**
 * Form controller for blog posts.
 * <P>
 * Initial Date: Aug 3, 2009 <br>
 * 
 * @author gwassmann
 */
public class BlogPostFormController extends FormBasicController {

    private final Item post;
    private final Feed blog;
    private TextElement title;
    private RichTextElement description, content;
    private DateChooser publishDateChooser;
    private IntegerElement hours;
    private TextElement mins;
    private FormLink draftLink;

    private final boolean currentlyDraft;

    /**
     * @param ureq
     * @param control
     */
    public BlogPostFormController(final UserRequest ureq, final WindowControl control, final Item post, final Feed blog, final Translator translator) {
        super(ureq, control);
        this.post = post;
        this.currentlyDraft = post.isDraft();
        this.blog = blog;
        setTranslator(translator);
        initForm(ureq);
    }

    /**
	 */
    @Override
    protected void doDispose() {
        // nothing to dispose
    }

    /**
	 */
    @Override
    protected void formOK(final UserRequest ureq) {
        // Update post. It is saved by the manager.
        setValues();
        if (!currentlyDraft || post.getModifierKey() > 0) {
            post.setModifierKey(ureq.getIdentity().getKey());
        }
        post.setDraft(false);
        fireEvent(ureq, Event.CHANGED_EVENT);
    }

    /**
	 * 
	 */
    private void setValues() {
        post.setTitle(title.getValue());
        post.setDescription(description.getValue());
        post.setContent(content.getValue());
        // The author is set already
        post.setLastModified(new Date());
        int hh = 0;
        try {
            hh = hours.getIntValue();
        } catch (final Exception e) {
            hh = 0;
        }
        int mm;
        try {
            mm = Integer.parseInt(mins.getValue());
        } catch (final NumberFormatException e) {
            mm = 0;
        }
        long time;
        if (publishDateChooser.getDate() != null) {
            time = publishDateChooser.getDate().getTime() + hh * Timer.ONE_HOUR + mm * Timer.ONE_MINUTE;
        } else {
            time = new Date().getTime() + hh * Timer.ONE_HOUR + mm * Timer.ONE_MINUTE;
        }
        post.setPublishDate(new Date(time));
    }

    /**
	 */
    @Override
    protected void formCancelled(final UserRequest ureq) {
        fireEvent(ureq, Event.CANCELLED_EVENT);
    }

    /**
     * org.olat.presentation.framework.components.form.flexible.FormItem, org.olat.presentation.framework.components.form.flexible.impl.FormEvent)
     */
    @Override
    protected void formInnerEvent(final UserRequest ureq, final FormItem source, final FormEvent event) {
        if (source == draftLink) {
            setValues();
            if (!currentlyDraft || post.getModifierKey() > 0) {
                post.setModifierKey(ureq.getIdentity().getKey());
            }
            post.setDraft(true);
            this.fireEvent(ureq, Event.CHANGED_EVENT);
        }
    }

    /**
     * org.olat.presentation.framework.control.Controller, org.olat.presentation.framework.UserRequest)
     */
    @Override
    protected void initForm(final FormItemContainer formLayout, final Controller listener, final UserRequest ureq) {
        this.setFormTitle("feed.edit.item");
        this.setFormContextHelp(this.getClass().getPackage().getName(), "post_form_help.html", "chelp.hover.form");

        title = uifactory.addTextElement("title", "feed.title.label", 256, post.getTitle(), this.flc);
        title.setMandatory(true);
        title.setNotEmptyCheck("feed.form.field.is_mandatory");

        final VFSContainer baseDir = FeedManager.getInstance().getItemContainer(post, blog);
        boolean fullProfileDescription = false;
        // Description
        description = uifactory.addRichTextElementForStringData("description", "feed.form.description", post.getDescription(), 8, -1, false, fullProfileDescription,
                baseDir, null, formLayout, ureq.getUserSession(), getWindowControl());
        final RichTextConfiguration descRichTextConfig = description.getEditorConfiguration();
        // set upload dir to the media dir
        descRichTextConfig.setFileBrowserUploadRelPath("media");
        descRichTextConfig.disableMediaAndOlatMovieViewer(baseDir, fullProfileDescription, 2);

        boolean fullProfileContent = false;
        // Content
        content = uifactory.addRichTextElementForStringData("content", "blog.form.content", post.getContent(), 18, -1, false, fullProfileContent, baseDir, null,
                formLayout, ureq.getUserSession(), getWindowControl());
        final RichTextConfiguration richTextConfig = content.getEditorConfiguration();
        // set upload dir to the media dir
        richTextConfig.setFileBrowserUploadRelPath("media");
        richTextConfig.disableMediaAndOlatMovieViewer(baseDir, fullProfileContent, 2);

        final FormLayoutContainer dateAndTimeLayout = FormLayoutContainer.createHorizontalFormLayout("feed.publish.date", getTranslator());
        formLayout.add(dateAndTimeLayout);
        dateAndTimeLayout.setLabel("feed.publish.date", null);
        dateAndTimeLayout.setMandatory(true);
        publishDateChooser = uifactory.addDateChooser("publishDateChooser", null, null, dateAndTimeLayout);
        publishDateChooser.setNotEmptyCheck("feed.publish.date.is.required");
        publishDateChooser.setValidDateCheck("feed.publish.date.invalid");
        final Calendar cal = Calendar.getInstance(ureq.getLocale());
        if (post.getPublishDate() != null) {
            cal.setTime(post.getPublishDate());
        }
        publishDateChooser.setDate(cal.getTime());
        hours = uifactory.addIntegerElement("hour", null, cal.get(Calendar.HOUR_OF_DAY), dateAndTimeLayout);
        hours.setDisplaySize(2);
        hours.setMaxLength(2);

        String minutesIn2digits = Long.toString(cal.get(Calendar.MINUTE));
        if (minutesIn2digits.length() == 1) {
            // always show two digits for minutes
            minutesIn2digits = '0' + minutesIn2digits;
        }
        uifactory.addStaticTextElement("timeSeparator", null, ":", dateAndTimeLayout);
        // dTextElement("mins", cal.get(Calendar.MINUTE), dateAndTimeLayout);
        mins = uifactory.addTextElement("mins", null, 2, minutesIn2digits, dateAndTimeLayout);
        mins.setDisplaySize(2);
        mins.setRegexMatchCheck("\\d*", "feed.form.minutes.error");
        mins.setNotEmptyCheck("feed.form.minutes.error");

        uifactory.addStaticTextElement("o.clock", null, translate("feed.publish.time.o.clock"), dateAndTimeLayout);

        // Submit and cancel buttons
        final FormLayoutContainer buttonLayout = FormLayoutContainer.createButtonLayout("button_layout", getTranslator());
        this.flc.add(buttonLayout);

        uifactory.addFormSubmitButton("feed.publish", buttonLayout);
        draftLink = uifactory.addFormLink("feed.save.as.draft", buttonLayout, Link.BUTTON);
        draftLink.addActionListener(this, FormEvent.ONCLICK);
        uifactory.addFormCancelButton("cancel", buttonLayout, ureq, getWindowControl());
    }
}
