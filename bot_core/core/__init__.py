from core.dialogue_manager import DialogueManager
from core.utils import RESOURCE_PATH

BOT = None


def start_bot():
    global BOT
    BOT = DialogueManager(RESOURCE_PATH)


def get_bot():
    if BOT is None:
        start_bot()
    return BOT
