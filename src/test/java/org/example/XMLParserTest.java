package org.example;

import org.example.Comic.*;
import org.example.XML.XMLParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class XMLParserTest {

    @Test
    void parseSimpleComicStructure() throws Exception {
        String xml = "<comic>"
                + "<figures>"
                + "<figure><name>John</name></figure>"
                + "</figures>"
                + "<scenes>"
                + "<scene><panel><setting>Park</setting></panel></scene>"
                + "</scenes>"
                + "</comic>";

        Comic comic = XMLParser.parseComic(xml);

        assertEquals(1, comic.getFigures().size());
        assertEquals("John", comic.getFigures().get(0).getName());
        assertEquals("Park", comic.getScenes().get(0).getPanels().get(0).getSetting());
    }

    @Test
    void parseMinimalPanel() throws Exception {
        String xml = """
            <comic>
                <scenes>
                    <scene>
                        <panel>
                            <setting>empty</setting>
                        </panel>
                    </scene>
                </scenes>
            </comic>""";

        Comic comic = XMLParser.parseComic(xml);
        Panel panel = comic.getScenes().get(0).getPanels().get(0);

        assertEquals("empty", panel.getSetting());
        assertNull(panel.getLeftSide());
        assertNull(panel.getRightSide());
    }

    @Test
    void parsePanelWithAllSides() throws Exception {
        String xml = """
            <comic>
                <scenes>
                    <scene>
                        <panel>
                            <left>
                                <balloon status="speaking">Hi</balloon>
                            </left>
                            <middle>
                                <figure><name>MiddleFig</name></figure>
                            </middle>
                            <right>
                                <balloon status="error">Missing</balloon>
                            </right>
                        </panel>
                    </scene>
                </scenes>
            </comic>""";

        Comic comic = XMLParser.parseComic(xml);
        Panel panel = comic.getScenes().get(0).getPanels().get(0);

        assertNotNull(panel.getLeftSide());
        assertNotNull(panel.getMiddleSide());
        assertNotNull(panel.getRightSide());

        assertEquals("speaking", panel.getLeftSide().getBalloonStatus());
        assertEquals("MiddleFig", panel.getMiddleSide().getPanelFigure().getName());
        assertEquals("error", panel.getRightSide().getBalloonStatus());
    }

    @Test
    void parsePanelWithEmptyBalloon() throws Exception {
        String xml = """
        <comic>
            <scenes>
                <scene>
                    <panel>
                        <left>
                            <balloon status=""/>
                        </left>
                    </panel>
                </scene>
            </scenes>
        </comic>""";

        Comic comic = XMLParser.parseComic(xml);
        PanelSide side = comic.getScenes().get(0).getPanels().get(0).getLeftSide();

        assertEquals("", side.getBalloonStatus());
        assertNull(side.getBalloonContent());
    }

    @Test
    void handleMissingFigureAttributes() throws Exception {
        String xml = """
        <comic>
            <figures>
                <figure>
                    <name>Nameless</name>
                </figure>
            </figures>
        </comic>""";

        Comic comic = XMLParser.parseComic(xml);
        Figure figure = comic.getFigures().get(0);

        assertNull(figure.getId());
        assertNull(figure.getSkin());
        assertEquals("Nameless", figure.getName());
    }
}
