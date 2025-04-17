package org.example;

import org.example.Comic.Panel;
import org.example.Comic.PanelSide;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PanelTest {

    @Test
    void panelDeepCopyRetainsAllProperties() {
        // Setup original panel
        Panel original = new Panel();
        original.setAbove("sky");
        original.setBelow("ground");
        original.setBorder("black");
        original.setSetting("park");

        PanelSide left = new PanelSide();
        left.setBalloonStatus("speaking");
        left.setBalloonContent("Hello!");
        original.setLeftSide(left);

        // Create copy
        Panel copy = original.deepCopy();

        // Verify properties
        assertEquals(original.getAbove(), copy.getAbove());
        assertEquals(original.getBelow(), copy.getBelow());
        assertEquals(original.getBorder(), copy.getBorder());
        assertEquals(original.getSetting(), copy.getSetting());

        // Verify deep copy of sides
        assertNotSame(original.getLeftSide(), copy.getLeftSide());
        assertEquals("Hello!", copy.getLeftSide().getBalloonContent());
    }

    @Test
    void panelSideCopyRetainsBalloonState() {
        PanelSide original = new PanelSide();
        original.setBalloonStatus("thinking");
        original.setBalloonContent("Hmm...");

        PanelSide copy = original.copy();

        assertEquals(original.getBalloonStatus(), copy.getBalloonStatus());
        assertEquals(original.getBalloonContent(), copy.getBalloonContent());
        assertNotSame(original, copy);
    }

    @Test
    void panelHandlesAllThreeSides() {
        Panel panel = new Panel();
        PanelSide left = new PanelSide();
        PanelSide middle = new PanelSide();
        PanelSide right = new PanelSide();

        panel.setLeftSide(left);
        panel.setMiddleSide(middle);
        panel.setRightSide(right);

        assertTrue(panel.hasLeft());
        assertTrue(panel.hasMiddle());
        assertTrue(panel.hasRight());
    }

    @Test
    void panelSideCopy() {
        PanelSide original = new PanelSide();
        original.setBalloonContent("Hello");
        PanelSide copy = original.copy();

        assertEquals(original.getBalloonContent(), copy.getBalloonContent());
        assertNotSame(original, copy);
    }
}