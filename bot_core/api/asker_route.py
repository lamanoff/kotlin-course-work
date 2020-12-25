import typing as ty

from chatterbot.conversation import Statement
from fastapi import APIRouter, Depends

from api.utils import get_bot

ask_route = r = APIRouter()


@r.get('/getAnswer')
async def get_answer(question, bot=Depends(get_bot)) -> ty.Dict:
    try:
        response_from_bot = bot.generate_answer(question)
        tag = "social"
        if isinstance(response_from_bot, Statement):
            answer = response_from_bot
        else:
            answer, tag = bot.generate_answer(question)
    except Exception as exc:
        return {'status': 'error', 'reason': str(exc)}
    return {'status': 'ok', 'answer': str(answer), 'tag': tag}
