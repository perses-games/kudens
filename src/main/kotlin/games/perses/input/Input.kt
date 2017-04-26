package games.perses.input

import games.perses.game.Game
import org.w3c.dom.Element
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
import kotlin.js.Date

/**
 * User: rnentjes
 * Date: 18-5-16
 * Time: 12:18
 */

enum class KeyCode(val keyCode: Int) {
    BACKSPACE(8),
    TAB(9),
    ENTER(13),
    SHIFT(16),
    CTRL(17),
    ALT(18),
    PAUSE_BREAK(19),
    CAPS_LOCK(20),
    ESCAPE(27),
    SPACE(32),
    PAGE_UP(33),
    PAGE_DOWN(34),
    END(35),
    HOME(36),
    LEFT_ARROW(37),
    UP_ARROW(38),
    RIGHT_ARROW(39),
    DOWN_ARROW(40),
    INSERT(45),
    DELETE(46),
    NR_0(48),
    NR_1(49),
    NR_2(50),
    NR_3(51),
    NR_4(52),
    NR_5(53),
    NR_6(54),
    NR_7(55),
    NR_8(56),
    NR_9(57),
    A(65),
    B(66),
    C(67),
    D(68),
    E(69),
    F(70),
    G(71),
    H(72),
    I(73),
    J(74),
    K(75),
    L(76),
    M(77),
    N(78),
    O(79),
    P(80),
    Q(81),
    R(82),
    S(83),
    T(84),
    U(85),
    V(86),
    W(87),
    X(88),
    Y(89),
    Z(90),
    LEFT_WINDOW_KEY(91),
    RIGHT_WINDOW_KEY(92),
    SELECT_KEY(93),
    NUMPAD_0(96),
    NUMPAD_1(97),
    NUMPAD_2(98),
    NUMPAD_3(99),
    NUMPAD_4(100),
    NUMPAD_5(101),
    NUMPAD_6(102),
    NUMPAD_7(103),
    NUMPAD_8(104),
    NUMPAD_9(105),
    MULTIPLY(106),
    ADD(107),
    SUBTRACT(109),
    DECIMAL_POINT(110),
    DIVIDE(111),
    F1(112),
    F2(113),
    F3(114),
    F4(115),
    F5(116),
    F6(117),
    F7(118),
    F8(119),
    F9(120),
    F10(121),
    F11(122),
    F12(123),
    NUM_LOCK(144),
    SCROLL_LOCK(145),
    SEMI_COLON(186),
    EQUAL_SIGN(187),
    COMMA(188),
    DASH(189),
    PERIOD(190),
    FORWARD_SLASH(191),
    GRAVE_ACCENT(192),
    OPEN_BRACKET(219),
    BACK_SLASH(220),
    CLOSE_BRAKET(221),
    SINGLE_QUOTE(222),

    ESC(27),
    LEFT(37),
    UP(38),
    DOWN(40),
    RIGHT(39),
    MINUS(109),
    PLUS(107),
}

interface InputProcessor {

    fun keyPressed(charCode: Int)

    fun keyDown(keyCode: Int)

    fun keyUp(keyCode: Int)

    fun pointerClick(pointer: Int, x: Float, y: Float)

    fun mouseMove(x: Float, y: Float)

}

open class EmptyInputProcessor : InputProcessor {

    override fun pointerClick(pointer: Int, x: Float, y: Float) { }

    override fun keyDown(keyCode: Int) { }

    override fun keyPressed(charCode: Int) { }

    override fun keyUp(keyCode: Int) { }

    override fun mouseMove(x: Float, y: Float) { }
}

object Input {

    private val keys: MutableMap<Int, Double> = HashMap()
    private var inputProcesser: InputProcessor = EmptyInputProcessor()

    init {
        val body = document.body
        if (body != null) {
            body.onkeydown = {
                keyDown(it)
            }

            body.onkeyup = {
                keyUp(it)
            }

            body.onkeypress = {
                keyPress(it)
            }

            body.onclick = {
                mouseClick(it)
            }

            body.onmousedown = {
                mouseMove(it)
            }

            body.onmouseup = {
                mouseMove(it)
            }

            body.onmousemove = {
                mouseMove(it)
            }
        } else {
            console.log("Can't register key events, document.body is null!?")
        }
    }

    fun setInputProcessor(processor: InputProcessor) {
        this.inputProcesser = processor
    }

    private fun keyDown(key: Event) {
        if (key is KeyboardEvent) {
            keys.put(key.keyCode, Date().getTime())

            inputProcesser.keyDown(key.keyCode)
        }
    }

    private fun keyUp(key: Event) {
        if (key is KeyboardEvent) {
            inputProcesser.keyUp(key.keyCode)

            keys.remove(key.keyCode)
        }
    }

    private fun keyPress(key: Event) {
        if (key is KeyboardEvent) {
            inputProcesser.keyPressed(key.charCode)
        }
    }

    private fun mouseClick(event: Event) {
        if (event is MouseEvent) {
            val vx: Float = Game.view.screenToGameCoordX(event.getX(Game.html.container).toFloat())
            val vy: Float = Game.view.screenToGameCoordY(event.getY(Game.html.container).toFloat())

            inputProcesser.pointerClick(event.button.toInt(), vx, vy)
        }
    }

    private fun mouseMove(event: Event) {
        if (event is MouseEvent) {
            val vx: Float = Game.view.screenToGameCoordX(event.getX(Game.html.container).toFloat())
            val vy: Float = Game.view.screenToGameCoordY(event.getY(Game.html.container).toFloat())

            inputProcesser.mouseMove(vx, vy)
        }
    }

    fun isDown(keyCode: Int) = keys.containsKey(keyCode)

    fun isDown(keyCode: KeyCode) = keys.containsKey(keyCode.keyCode)

    fun wasPressed(keyCode: Int, delta: Double): Boolean {
        val time = keys[keyCode]

        return (time != null && time > (Date().getTime() - delta))
    }

}

fun MouseEvent.getX(element: Element) = this.pageX - element.getBoundingClientRect().left

fun MouseEvent.getY(element: Element) = this.pageY - element.getBoundingClientRect().top
