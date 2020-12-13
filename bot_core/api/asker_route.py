import typing as ty
from fastapi import APIRouter, Depends

from api.utils import get_bot

ask_route = r = APIRouter()


@r.get('/getAnswer')
async def get_answer(question, bot=Depends(get_bot)) -> ty.Dict:
    try:
        answer = bot.generate_answer(question)
    except Exception as exc:
        return {'status': 'error', 'reason': str(exc)}
    return {'status': 'ok', 'response': str(answer)}
