package org.vaadin.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryProvider;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {


	private FullCalendar calendar;	
	private Entry selected;
	
    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired GreetService service) {
        createCalendarInstance();
    	addDemoEntrys();

        setSizeFull();
        add(calendar);
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        setFlexGrow(1, calendar);
    }
    private void createCalendarInstance() {
    	calendar = FullCalendarBuilder.create().build();
    	
    	calendar.changeView(CalendarViewImpl.DAY_GRID_MONTH);

    	calendar.setEntryDidMountCallback("" +
    			"function (info) {" +
    			"    console.log('---- hello setEntryDidMountCallback');" +
    			"}");
        calendar.setEntryContentCallback("" +
            "function (info) {" +
            "    console.log('---- hi setEntryContentCallback');" +
//            "    info.backgroundColor = info.event.getCustomProperty('selected', false) ? 'lightblue' : 'lightgreen';" +
            "}");

//        calendar.addEntryClickedListener(e -> {
//            Entry oldSelected = this.selected;
//            oldSelected.setCustomProperty("selected", false);
//            this.selected = e.getEntry();
//            this.selected.setCustomProperty("selected", true);
//            calendar.updateEntries(oldSelected, this.selected);
//        });

        calendar.setHeightByParent();
    }
	
	private void addDemoEntrys() {

		List<Entry> entries = new ArrayList<Entry>();
		
    	for (int i = 1; i < 10; i++) {
            LocalDate now = LocalDate.now();
            LocalDate start = now.withDayOfMonth((int)(Math.random() * 28) + 1);
            LocalDate end = start.plusDays(1);
            Entry entry = new Entry(UUID.randomUUID().toString());
            entry.setColor("lightgreen");
            entry.setTitle("Entry " + i);
            entry.setStart(start.atStartOfDay());
            entry.setEnd(end.atTime(LocalTime.MAX));
            if (i == 1) {
                entry.setCustomProperty("selected", true);
                selected = entry;
            }
            entries.add(entry);
        }
    	
    	calendar.setEntryProvider(EntryProvider.lazyInMemoryFromItems(entries));
    }
}
